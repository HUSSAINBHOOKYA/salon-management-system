package com.salonmanagementsystem.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.salonmanagementsystem.dto.AIRequest;
import com.salonmanagementsystem.dto.AIResponse;
import com.salonmanagementsystem.entity.ProductClass;
import com.salonmanagementsystem.entity.SalonService;
import com.salonmanagementsystem.enums.Gender;
import com.salonmanagementsystem.enums.ServiceStatus;
import com.salonmanagementsystem.repository.ProductRepository;
import com.salonmanagementsystem.repository.ServiceRepository;
import com.salonmanagementsystem.service.AIService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AIServiceImpl implements AIService {

    private final ProductRepository productRepository;
    private final ServiceRepository serviceRepository;

    // =========================================================
    // MAIN AI RECOMMENDATION
    // =========================================================

    @Override
    public AIResponse getRecommendation(AIRequest request) {

        if (request == null
                || request.getMessage() == null
                || request.getMessage().trim().isEmpty()) {

            return new AIResponse(
                    "Please tell me what you need help with. "
                            + "For example, you can ask about a haircut, "
                            + "hair concern, facial, grooming service, "
                            + "or SalonHub product."
            );
        }

        String userMessage = request.getMessage().trim();

        // -----------------------------------------------------
        // Detect user intent FIRST.
        //
        // This is important because:
        //
        // "My hair is dry and frizzy. Which haircut should I use?"
        //
        // must be treated as HAIRCUT, not HAIR_TREATMENT.
        // -----------------------------------------------------

        AIIntent intent = detectIntent(userMessage);

        Gender requestedGender = detectGender(userMessage);

        boolean productQuestion =
                isProductRelatedQuestion(userMessage);

        boolean serviceQuestion =
                isServiceRelatedQuestion(userMessage);

        // -----------------------------------------------------
        // Service intent without gender
        // -----------------------------------------------------

        if (isServiceIntent(intent)
                && requestedGender == null) {

            return new AIResponse(
                    buildGenderQuestion(userMessage)
            );
        }

        // -----------------------------------------------------
        // Load products only when the customer actually wants
        // a product recommendation.
        // -----------------------------------------------------

        List<ProductClass> products = new ArrayList<>();

        if (productQuestion || intent == AIIntent.PRODUCT_CONCERN) {
            products = productRepository.findAll();
        }

        // -----------------------------------------------------
        // Load services only when the customer actually wants
        // a service recommendation.
        // -----------------------------------------------------

        List<SalonService> services = new ArrayList<>();

        if (isServiceIntent(intent) || serviceQuestion) {

            if (requestedGender != null) {

                services = serviceRepository.findByGenderAndStatus(
                        requestedGender,
                        ServiceStatus.ACTIVE
                );
            }
        }

        // -----------------------------------------------------
        // PRODUCT RECOMMENDATIONS
        // -----------------------------------------------------

        List<ProductRecommendation> productRecommendations =
                new ArrayList<>();

        if (productQuestion || intent == AIIntent.PRODUCT_CONCERN) {

            productRecommendations =
                    findProducts(
                            userMessage,
                            products,
                            requestedGender,
                            intent
                    );
        }

        // -----------------------------------------------------
        // SERVICE RECOMMENDATIONS
        // -----------------------------------------------------

        List<ServiceRecommendation> serviceRecommendations =
                new ArrayList<>();

        if (isServiceIntent(intent) || serviceQuestion) {

            serviceRecommendations =
                    findServices(
                            userMessage,
                            services,
                            intent
                    );
        }

        // -----------------------------------------------------
        // FINAL RESPONSE
        // -----------------------------------------------------

        String response = buildResponse(
                userMessage,
                requestedGender,
                intent,
                productRecommendations,
                serviceRecommendations
        );

        return new AIResponse(response);
    }

    // =========================================================
    // INTENT DETECTION
    // =========================================================

    private AIIntent detectIntent(String message) {

        String text = normalize(message);

        // -----------------------------------------------------
        // PRODUCT QUESTION HAS HIGH PRIORITY
        // -----------------------------------------------------

        if (isProductRelatedQuestion(text)) {

            return AIIntent.PRODUCT_CONCERN;
        }

        // -----------------------------------------------------
        // EXPLICIT HAIRCUT / HAIRSTYLE
        //
        // Must come BEFORE hair treatment.
        //
        // Example:
        // "dry frizzy hair which haircut should I use?"
        //
        // Correct intent = HAIRCUT
        // -----------------------------------------------------

        if (containsAny(
                text,
                "haircut",
                "hair cut",
                "hairstyle",
                "hair style",
                "which haircut",
                "which hair cut",
                "which hairstyle",
                "short hairstyle",
                "short hair",
                "keep it short",
                "cut my hair",
                "hair cut style",
                "haircut style",
                "fade haircut",
                "bob haircut",
                "pixie haircut",
                "layered haircut",
                "layers"
        )) {

            return AIIntent.HAIRCUT;
        }

        // -----------------------------------------------------
        // BEARD / GROOMING
        // -----------------------------------------------------

        if (containsAny(
                text,
                "beard",
                "beard style",
                "beard trim",
                "beard grooming",
                "shaving",
                "shave",
                "mustache",
                "moustache",
                "grooming"
        )) {

            return AIIntent.BEARD_GROOMING;
        }

        // -----------------------------------------------------
        // MAKEUP
        // -----------------------------------------------------

        if (containsAny(
                text,
                "makeup",
                "make up",
                "bridal makeup",
                "party makeup",
                "engagement makeup",
                "eye makeup",
                "lipstick",
                "foundation",
                "contour"
        )) {

            return AIIntent.MAKEUP;
        }

        // -----------------------------------------------------
        // NAIL
        // -----------------------------------------------------

        if (containsAny(
                text,
                "nail",
                "nails",
                "nail art",
                "gel polish",
                "manicure",
                "pedicure"
        )) {

            return AIIntent.NAIL;
        }

        // -----------------------------------------------------
        // FACIAL / SKIN
        // -----------------------------------------------------

        if (containsAny(
                text,
                "facial",
                "face treatment",
                "skin treatment",
                "skin care",
                "skin care service",
                "dry skin",
                "oily skin",
                "acne",
                "pimples",
                "blackheads",
                "whiteheads",
                "glowing skin",
                "dull skin",
                "skin brightening",
                "skin hydration",
                "hydrating facial"
        )) {

            return AIIntent.FACIAL;
        }

        // -----------------------------------------------------
        // HAIR TREATMENT
        //
        // This comes after explicit haircut detection.
        // -----------------------------------------------------

        if (containsAny(
                text,
                "hair treatment",
                "hair spa",
                "hair smoothening",
                "smoothening",
                "keratin",
                "hair keratin",
                "hair repair",
                "hair damage",
                "damaged hair",
                "dry hair",
                "frizzy hair",
                "frizzy",
                "hair fall",
                "hair loss",
                "split ends",
                "dandruff treatment",
                "scalp treatment"
        )) {

            return AIIntent.HAIR_TREATMENT;
        }

        // -----------------------------------------------------
        // HAIR COLOR
        // -----------------------------------------------------

        if (containsAny(
                text,
                "hair color",
                "hair colour",
                "hair coloring",
                "hair colouring",
                "global color",
                "global colour",
                "highlights",
                "highlight",
                "balayage",
                "ombre",
                "ombre hair"
        )) {

            return AIIntent.HAIR_COLOR;
        }

        // -----------------------------------------------------
        // SPA
        // -----------------------------------------------------

        if (containsAny(
                text,
                "spa",
                "massage",
                "body massage",
                "relaxation",
                "body treatment",
                "wellness"
        )) {

            return AIIntent.SPA;
        }

        // -----------------------------------------------------
        // GENERAL SERVICE
        // -----------------------------------------------------

        if (isServiceRelatedQuestion(text)) {

            return AIIntent.GENERAL_SERVICE;
        }

        // -----------------------------------------------------
        // If a concern is clearly mentioned but the customer
        // did not say product/service, treat it as a product
        // concern.
        //
        // Example:
        // "I have dandruff"
        // -----------------------------------------------------

        if (containsAny(
                text,
                "dandruff",
                "dry scalp",
                "itchy scalp",
                "scalp problem",
                "dry hair",
                "frizzy hair",
                "damaged hair",
                "hair fall",
                "hair loss",
                "dry skin",
                "oily skin",
                "acne",
                "pimples"
        )) {

            return AIIntent.PRODUCT_CONCERN;
        }

        return AIIntent.UNKNOWN;
    }

    // =========================================================
    // SERVICE INTENT CHECK
    // =========================================================

    private boolean isServiceIntent(AIIntent intent) {

        return intent == AIIntent.HAIRCUT
                || intent == AIIntent.HAIR_TREATMENT
                || intent == AIIntent.HAIR_COLOR
                || intent == AIIntent.FACIAL
                || intent == AIIntent.MAKEUP
                || intent == AIIntent.NAIL
                || intent == AIIntent.BEARD_GROOMING
                || intent == AIIntent.SPA
                || intent == AIIntent.GENERAL_SERVICE;
    }

    // =========================================================
    // PRODUCT QUESTION DETECTION
    // =========================================================

    private boolean isProductRelatedQuestion(String message) {

        String text = normalize(message);

        return containsAny(
                text,
                "product",
                "products",
                "which product",
                "what product",
                "recommend a product",
                "recommend product",
                "suggest a product",
                "suggest product",
                "which shampoo",
                "what shampoo",
                "which conditioner",
                "what conditioner",
                "which cream",
                "what cream",
                "which serum",
                "what serum",
                "which mask",
                "what mask",
                "which cleanser",
                "what cleanser",
                "which face wash",
                "what face wash",
                "which hair oil",
                "what hair oil"
        );
    }

    // =========================================================
    // SERVICE QUESTION DETECTION
    // =========================================================

    private boolean isServiceRelatedQuestion(String message) {

        String text = normalize(message);

        return containsAny(
                text,
                "service",
                "services",
                "which service",
                "what service",
                "recommend a service",
                "recommend service",
                "suggest a service",
                "suggest service",
                "haircut",
                "hair cut",
                "hairstyle",
                "hair style",
                "facial",
                "makeup",
                "nail",
                "nails",
                "manicure",
                "pedicure",
                "spa",
                "massage",
                "beard",
                "shave",
                "grooming",
                "hair treatment",
                "hair spa",
                "smoothening",
                "keratin",
                "hair color",
                "hair colour"
        );
    }

    // =========================================================
    // GENDER QUESTION
    // =========================================================

    private String buildGenderQuestion(String userMessage) {

        String message = normalize(userMessage);

        // -----------------------------------------------------
        // Haircut
        // -----------------------------------------------------

        if (containsAny(
                message,
                "haircut",
                "hair cut",
                "hairstyle",
                "hair style",
                "short hair",
                "keep it short",
                "fade",
                "bob",
                "pixie",
                "layer",
                "layers"
        )) {

            return "Sure! I can help you find a suitable "
                    + "hairstyle or haircut. "
                    + "Is this for men or women?";
        }

        // -----------------------------------------------------
        // Hair treatment
        // -----------------------------------------------------

        if (containsAny(
                message,
                "hair treatment",
                "hair spa",
                "smoothening",
                "keratin",
                "dry hair",
                "frizzy hair",
                "hair damage",
                "dandruff treatment",
                "hair fall"
        )) {

            return "Sure! I can help you find a suitable "
                    + "hair treatment. "
                    + "Is this for men or women?";
        }

        // -----------------------------------------------------
        // Skin / facial
        // -----------------------------------------------------

        if (containsAny(
                message,
                "skin",
                "facial",
                "face",
                "acne",
                "pimples",
                "dry skin",
                "oily skin",
                "glowing skin",
                "dull skin",
                "skin care"
        )) {

            return "Sure! I can help you find a suitable "
                    + "skin or facial service. "
                    + "Is this for men or women?";
        }

        // -----------------------------------------------------
        // Grooming
        // -----------------------------------------------------

        if (containsAny(
                message,
                "grooming",
                "groom",
                "beard",
                "shave",
                "trim"
        )) {

            return "Sure! I can help you find a suitable "
                    + "grooming service. "
                    + "Is this for men or women?";
        }

        // -----------------------------------------------------
        // General
        // -----------------------------------------------------

        return "Sure! I can help you find a suitable "
                + "SalonHub service. "
                + "Is this for men or women?";
    }

    // =========================================================
    // DETECT GENDER
    // =========================================================

    private Gender detectGender(String message) {

        String text = normalize(message);

        // -----------------------------------------------------
        // WOMEN
        // -----------------------------------------------------

        if (containsAny(
                text,
                "woman",
                "women",
                "female",
                "girl",
                "lady",
                "ladies",
                "for women",
                "for woman",
                "womens",
                "women's",
                "female customer"
        )) {

            return Gender.WOMEN;
        }

        // -----------------------------------------------------
        // MEN
        // -----------------------------------------------------

        if (containsAny(
                text,
                "man",
                "men",
                "male",
                "boy",
                "gentleman",
                "gentlemen",
                "for men",
                "for man",
                "mens",
                "men's",
                "male customer"
        )) {

            return Gender.MEN;
        }

        return null;
    }

    // =========================================================
    // FIND PRODUCTS
    // =========================================================

    private List<ProductRecommendation> findProducts(
            String userMessage,
            List<ProductClass> products,
            Gender requestedGender,
            AIIntent intent) {

        List<ProductRecommendation> recommendations =
                new ArrayList<>();

        String normalizedMessage =
                normalize(userMessage);

        Set<String> userKeywords =
                extractKeywords(normalizedMessage);

        for (ProductClass product : products) {

            if (product == null) {
                continue;
            }

            // -------------------------------------------------
            // Only available products
            // -------------------------------------------------

            if (!isProductAvailable(product)) {
                continue;
            }

            // -------------------------------------------------
            // Product gender
            //
            // Generic products are allowed for both MEN/WOMEN.
            // -------------------------------------------------

            ProductGender productGender =
                    detectProductGender(product);

            if (requestedGender != null
                    && productGender != ProductGender.UNISEX
                    && productGender != convertGender(requestedGender)) {

                continue;
            }

            // -------------------------------------------------
            // Product searchable text
            // -------------------------------------------------

            String searchableText =
                    buildProductSearchText(product);

            String normalizedProductText =
                    normalize(searchableText);

            Set<String> productKeywords =
                    extractKeywords(normalizedProductText);

            // -------------------------------------------------
            // Basic keyword matching
            // -------------------------------------------------

            int score =
                    calculateKeywordScore(
                            userKeywords,
                            productKeywords
                    );

            // -------------------------------------------------
            // Concern-specific matching
            // -------------------------------------------------

            score += calculateConcernScore(
                    normalizedMessage,
                    normalizedProductText
            );

            // -------------------------------------------------
            // Intent-specific matching
            // -------------------------------------------------

            score += calculateProductIntentScore(
                    normalizedMessage,
                    normalizedProductText,
                    intent
            );

            if (score > 0) {

                recommendations.add(
                        new ProductRecommendation(
                                product,
                                score
                        )
                );
            }
        }

        // -----------------------------------------------------
        // Sort:
        //
        // 1. AI relevance score
        // 2. Rating
        // 3. Review count
        // -----------------------------------------------------

        recommendations.sort(
                Comparator
                        .comparingInt(
                                ProductRecommendation::getScore
                        )
                        .reversed()
                        .thenComparing(
                                recommendation ->
                                        getRating(
                                                recommendation.getProduct()
                                        ),
                                Comparator.reverseOrder()
                        )
                        .thenComparing(
                                recommendation ->
                                        getReviewCount(
                                                recommendation.getProduct()
                                        ),
                                Comparator.reverseOrder()
                        )
        );

        return recommendations.stream()
                .limit(3)
                .toList();
    }

    // =========================================================
    // PRODUCT INTENT SCORE
    // =========================================================

    private int calculateProductIntentScore(
            String message,
            String productText,
            AIIntent intent) {

        int score = 0;

        // -----------------------------------------------------
        // DANDRUFF
        // -----------------------------------------------------

        if (containsAny(
                message,
                "dandruff",
                "dry scalp",
                "itchy scalp",
                "scalp problem"
        )) {

            if (containsAny(
                    productText,
                    "dandruff",
                    "anti dandruff",
                    "anti-dandruff",
                    "scalp",
                    "scalp care",
                    "scalp treatment"
            )) {

                score += 15;
            }

            if (containsAny(
                    productText,
                    "shampoo",
                    "purifying",
                    "clarifying",
                    "tea tree"
            )) {

                score += 8;
            }

            if (containsAny(
                    productText,
                    "hair care",
                    "hair treatment"
            )) {

                score += 3;
            }
        }

        // -----------------------------------------------------
        // DRY / FRIZZY HAIR
        // -----------------------------------------------------

        if (containsAny(
                message,
                "dry hair",
                "frizzy hair",
                "frizzy",
                "dry and frizzy",
                "damaged hair",
                "hair damage"
        )) {

            if (containsAny(
                    productText,
                    "hydrating",
                    "moisturizing",
                    "moisture",
                    "repair",
                    "repair mask",
                    "hair mask",
                    "anti frizz",
                    "anti-frizz",
                    "frizz control",
                    "hair treatment"
            )) {

                score += 12;
            }
        }

        // -----------------------------------------------------
        // HAIR FALL
        // -----------------------------------------------------

        if (containsAny(
                message,
                "hair fall",
                "hair loss",
                "hair shedding"
        )) {

            if (containsAny(
                    productText,
                    "hair fall",
                    "hair loss",
                    "scalp",
                    "hair treatment",
                    "hair care"
            )) {

                score += 12;
            }
        }

        // -----------------------------------------------------
        // DRY SKIN
        // -----------------------------------------------------

        if (containsAny(
                message,
                "dry skin",
                "dehydrated skin",
                "very dry skin"
        )) {

            if (containsAny(
                    productText,
                    "hydrating",
                    "hydration",
                    "moisturizing",
                    "moisturizer",
                    "moisture",
                    "dry skin",
                    "face cream",
                    "facial cleanser",
                    "cleanser"
            )) {

                score += 12;
            }
        }

        // -----------------------------------------------------
        // OILY SKIN
        // -----------------------------------------------------

        if (containsAny(
                message,
                "oily skin",
                "oil skin",
                "very oily"
        )) {

            if (containsAny(
                    productText,
                    "oil control",
                    "oily skin",
                    "clarifying",
                    "purifying",
                    "cleanser",
                    "face wash"
            )) {

                score += 12;
            }
        }

        // -----------------------------------------------------
        // ACNE / PIMPLES
        // -----------------------------------------------------

        if (containsAny(
                message,
                "acne",
                "pimples",
                "breakouts",
                "blackheads",
                "whiteheads"
        )) {

            if (containsAny(
                    productText,
                    "acne",
                    "pimple",
                    "clarifying",
                    "purifying",
                    "cleanser",
                    "face wash",
                    "oil control"
            )) {

                score += 12;
            }
        }

        // -----------------------------------------------------
        // GENERAL PRODUCT CONCERN
        // -----------------------------------------------------

        if (intent == AIIntent.PRODUCT_CONCERN) {

            if (containsAny(
                    productText,
                    "care",
                    "treatment",
                    "repair",
                    "cleanser",
                    "shampoo",
                    "mask",
                    "serum"
            )) {

                score += 2;
            }
        }

        return score;
    }

    // =========================================================
    // PRODUCT CONCERN SCORE
    // =========================================================

    private int calculateConcernScore(
            String message,
            String productText) {

        int score = 0;

        // -----------------------------------------------------
        // DANDRUFF
        // -----------------------------------------------------

        if (containsAny(message, "dandruff")) {

            if (containsAny(
                    productText,
                    "dandruff",
                    "anti dandruff",
                    "anti-dandruff",
                    "scalp",
                    "scalp care",
                    "scalp treatment"
            )) {

                score += 10;
            }
        }

        // -----------------------------------------------------
        // DRY HAIR
        // -----------------------------------------------------

        if (containsAny(
                message,
                "dry hair",
                "dry and frizzy",
                "frizzy hair"
        )) {

            if (containsAny(
                    productText,
                    "repair",
                    "moisture",
                    "hydrating",
                    "hair mask",
                    "hair treatment",
                    "frizz"
            )) {

                score += 8;
            }
        }

        // -----------------------------------------------------
        // DRY SKIN
        // -----------------------------------------------------

        if (containsAny(
                message,
                "dry skin",
                "very dry skin"
        )) {

            if (containsAny(
                    productText,
                    "hydrating",
                    "hydration",
                    "moisturizing",
                    "moisture",
                    "dry skin",
                    "cleanser"
            )) {

                score += 8;
            }
        }

        // -----------------------------------------------------
        // OILY SKIN
        // -----------------------------------------------------

        if (containsAny(
                message,
                "oily skin"
        )) {

            if (containsAny(
                    productText,
                    "oil control",
                    "oily skin",
                    "clarifying",
                    "purifying",
                    "cleanser"
            )) {

                score += 8;
            }
        }

        return score;
    }

    // =========================================================
    // PRODUCT GENDER
    // =========================================================

    private ProductGender detectProductGender(
            ProductClass product) {

        String text =
                normalize(buildProductSearchText(product));

        boolean womenSpecific =
                containsAny(
                        text,
                        "women",
                        "woman",
                        "womens",
                        "women's",
                        "female",
                        "ladies",
                        "lady",
                        "makeup",
                        "lipstick",
                        "nail polish",
                        "nail art",
                        "manicure",
                        "pedicure"
                );

        boolean menSpecific =
                containsAny(
                        text,
                        "men's",
                        "mens",
                        "male",
                        "for men",
                        "for man",
                        "beard",
                        "shaving",
                        "shave",
                        "mustache",
                        "moustache"
                );

        // If the product explicitly mentions both, treat it
        // as generic/unisex.

        if (womenSpecific && menSpecific) {
            return ProductGender.UNISEX;
        }

        if (womenSpecific) {
            return ProductGender.WOMEN;
        }

        if (menSpecific) {
            return ProductGender.MEN;
        }

        return ProductGender.UNISEX;
    }

    // =========================================================
    // FIND SERVICES
    // =========================================================

    private List<ServiceRecommendation> findServices(
            String userMessage,
            List<SalonService> services,
            AIIntent intent) {

        List<ServiceRecommendation> recommendations =
                new ArrayList<>();

        String normalizedMessage =
                normalize(userMessage);

        Set<String> userKeywords =
                extractKeywords(normalizedMessage);

        for (SalonService service : services) {

            if (service == null) {
                continue;
            }

            if (service.getStatus() != ServiceStatus.ACTIVE) {
                continue;
            }

            String searchableText =
                    buildServiceSearchText(service);

            String normalizedServiceText =
                    normalize(searchableText);

            Set<String> serviceKeywords =
                    extractKeywords(normalizedServiceText);

            int score =
                    calculateKeywordScore(
                            userKeywords,
                            serviceKeywords
                    );

            score += calculateServiceIntentScore(
                    normalizedMessage,
                    normalizedServiceText,
                    intent
            );

            if (score > 0) {

                recommendations.add(
                        new ServiceRecommendation(
                                service,
                                score
                        )
                );
            }
        }

        // -----------------------------------------------------
        // Sort:
        //
        // 1. AI relevance
        // 2. Lower price as secondary neutral catalog ordering
        // -----------------------------------------------------

        recommendations.sort(
                Comparator
                        .comparingInt(
                                ServiceRecommendation::getScore
                        )
                        .reversed()
                        .thenComparing(
                                recommendation ->
                                        getPrice(
                                                recommendation.getService()
                                        ),
                                Comparator.nullsLast(
                                        Comparator.naturalOrder()
                                )
                        )
        );

        return recommendations.stream()
                .limit(3)
                .toList();
    }

    // =========================================================
    // SERVICE INTENT SCORE
    // =========================================================

    private int calculateServiceIntentScore(
            String message,
            String serviceText,
            AIIntent intent) {

        int score = 0;

        switch (intent) {

            // -------------------------------------------------
            // HAIRCUT
            // -------------------------------------------------

            case HAIRCUT:

                if (containsAny(
                        serviceText,
                        "haircut",
                        "hair cut",
                        "hair cut and styling",
                        "hair styling",
                        "hairstyle",
                        "fade",
                        "bob",
                        "pixie",
                        "layer",
                        "layers",
                        "short hair",
                        "styling"
                )) {

                    score += 20;
                }

                // Explicit short-hair request
                if (containsAny(
                        message,
                        "short",
                        "keep it short",
                        "short hairstyle"
                )) {

                    if (containsAny(
                            serviceText,
                            "short",
                            "fade",
                            "bob",
                            "pixie",
                            "haircut",
                            "hair cut"
                    )) {

                        score += 8;
                    }
                }

                break;

            // -------------------------------------------------
            // HAIR TREATMENT
            // -------------------------------------------------

            case HAIR_TREATMENT:

                if (containsAny(
                        serviceText,
                        "treatment",
                        "hair treatment",
                        "hair spa",
                        "smoothening",
                        "keratin",
                        "repair",
                        "hair repair"
                )) {

                    score += 20;
                }

                if (containsAny(
                        message,
                        "dry",
                        "frizzy",
                        "damage",
                        "damaged"
                )) {

                    if (containsAny(
                            serviceText,
                            "smoothening",
                            "keratin",
                            "repair",
                            "spa",
                            "treatment"
                    )) {

                        score += 8;
                    }
                }

                break;

            // -------------------------------------------------
            // HAIR COLOR
            // -------------------------------------------------

            case HAIR_COLOR:

                if (containsAny(
                        serviceText,
                        "color",
                        "colour",
                        "global color",
                        "global colour",
                        "highlight",
                        "balayage",
                        "ombre"
                )) {

                    score += 20;
                }

                break;

            // -------------------------------------------------
            // FACIAL
            // -------------------------------------------------

            case FACIAL:

                if (containsAny(
                        serviceText,
                        "facial",
                        "face",
                        "skin",
                        "skin treatment",
                        "facial treatment"
                )) {

                    score += 20;
                }

                if (containsAny(
                        message,
                        "dry skin",
                        "very dry skin",
                        "dehydrated skin"
                )) {

                    if (containsAny(
                            serviceText,
                            "hydrating",
                            "hydration",
                            "moisture",
                            "facial",
                            "skin"
                    )) {

                        score += 8;
                    }
                }

                if (containsAny(
                        message,
                        "oily skin"
                )) {

                    if (containsAny(
                            serviceText,
                            "oil",
                            "clarifying",
                            "purifying",
                            "facial"
                    )) {

                        score += 8;
                    }
                }

                if (containsAny(
                        message,
                        "glowing skin",
                        "dull skin"
                )) {

                    if (containsAny(
                            serviceText,
                            "glow",
                            "brightening",
                            "facial"
                    )) {

                        score += 8;
                    }
                }

                break;

            // -------------------------------------------------
            // MAKEUP
            // -------------------------------------------------

            case MAKEUP:

                if (containsAny(
                        serviceText,
                        "makeup",
                        "make up",
                        "bridal",
                        "party makeup",
                        "engagement"
                )) {

                    score += 20;
                }

                break;

            // -------------------------------------------------
            // NAIL
            // -------------------------------------------------

            case NAIL:

                if (containsAny(
                        serviceText,
                        "nail",
                        "nails",
                        "manicure",
                        "pedicure",
                        "gel polish"
                )) {

                    score += 20;
                }

                break;

            // -------------------------------------------------
            // BEARD / GROOMING
            // -------------------------------------------------

            case BEARD_GROOMING:

                if (containsAny(
                        serviceText,
                        "beard",
                        "grooming",
                        "shave",
                        "shaving",
                        "trim",
                        "mustache",
                        "moustache"
                )) {

                    score += 20;
                }

                break;

            // -------------------------------------------------
            // SPA
            // -------------------------------------------------

            case SPA:

                if (containsAny(
                        serviceText,
                        "spa",
                        "massage",
                        "wellness",
                        "body treatment",
                        "relaxation"
                )) {

                    score += 20;
                }

                break;

            // -------------------------------------------------
            // GENERAL SERVICE
            // -------------------------------------------------

            case GENERAL_SERVICE:

                if (containsAny(
                        serviceText,
                        "service",
                        "hair",
                        "skin",
                        "facial",
                        "grooming",
                        "spa",
                        "nail",
                        "makeup"
                )) {

                    score += 5;
                }

                break;

            default:
                break;
        }

        return score;
    }

    // =========================================================
    // BUILD PRODUCT SEARCH TEXT
    // =========================================================

    private String buildProductSearchText(
            ProductClass product) {

        StringBuilder text = new StringBuilder();

        if (product.getProductName() != null) {
            text.append(product.getProductName()).append(" ");
        }

        if (product.getDescription() != null) {
            text.append(product.getDescription()).append(" ");
        }

        if (product.getCategory() != null) {
            text.append(product.getCategory()).append(" ");
        }

        if (product.getBrand() != null) {
            text.append(product.getBrand()).append(" ");
        }

        return text.toString();
    }

    // =========================================================
    // BUILD SERVICE SEARCH TEXT
    // =========================================================

    private String buildServiceSearchText(
            SalonService service) {

        StringBuilder text = new StringBuilder();

        if (service.getServiceName() != null) {
            text.append(service.getServiceName()).append(" ");
        }

        if (service.getDescription() != null) {
            text.append(service.getDescription()).append(" ");
        }

        if (service.getCategory() != null
                && service.getCategory().getCategoryName() != null) {

            text.append(
                    service.getCategory().getCategoryName()
            ).append(" ");
        }

        return text.toString();
    }

    // =========================================================
    // KEYWORD SCORE
    // =========================================================

    private int calculateKeywordScore(
            Set<String> userKeywords,
            Set<String> targetKeywords) {

        int score = 0;

        for (String keyword : userKeywords) {

            if (targetKeywords.contains(keyword)) {
                score += 2;
            }
        }

        return score;
    }

    // =========================================================
    // KEYWORD EXTRACTION
    // =========================================================

    private Set<String> extractKeywords(String text) {

        Set<String> keywords =
                new HashSet<>();

        if (text == null || text.isBlank()) {
            return keywords;
        }

        String cleaned =
                normalize(text)
                        .replaceAll("[^a-z0-9 ]", " ");

        String[] words =
                cleaned.split("\\s+");

        Set<String> stopWords = Set.of(
                "i",
                "me",
                "my",
                "mine",
                "the",
                "a",
                "an",
                "and",
                "or",
                "is",
                "are",
                "am",
                "was",
                "were",
                "have",
                "has",
                "had",
                "with",
                "for",
                "to",
                "of",
                "in",
                "on",
                "at",
                "from",
                "which",
                "what",
                "where",
                "when",
                "who",
                "how",
                "can",
                "could",
                "would",
                "should",
                "do",
                "does",
                "did",
                "please",
                "want",
                "need",
                "use",
                "recommend",
                "recommendation",
                "recommendations",
                "suggest",
                "suitable",
                "suit",
                "best",
                "good",
                "give",
                "tell"
                
        );

        for (String word : words) {

            if (word == null || word.isBlank()) {
                continue;
            }

            if (word.length() < 3) {
                continue;
            }

            if (stopWords.contains(word)) {
                continue;
            }

            keywords.add(word);
        }

        return keywords;
    }

    // =========================================================
    // PRODUCT AVAILABILITY
    // =========================================================

    private boolean isProductAvailable(
            ProductClass product) {

        if (product == null) {
            return false;
        }

        // Stock quantity must be available.
        if (product.getStockQuantity() != null
                && product.getStockQuantity() <= 0) {

            return false;
        }

        // Product status.
        if (product.getStatus() != null) {

            String status =
                    product.getStatus()
                            .name()
                            .toUpperCase(Locale.ROOT);

            if ("OUT_OF_STOCK".equals(status)
                    || "INACTIVE".equals(status)
                    || "DISABLED".equals(status)
                    || "UNAVAILABLE".equals(status)) {

                return false;
            }
        }

        return true;
    }

    // =========================================================
    // RESPONSE BUILDER
    // =========================================================

    private String buildResponse(
            String userMessage,
            Gender requestedGender,
            AIIntent intent,
            List<ProductRecommendation> productRecommendations,
            List<ServiceRecommendation> serviceRecommendations) {

        StringBuilder response =
                new StringBuilder();

        response.append(
                "Based on your SalonHub request, "
                        + "here are recommendations from our "
                        + "available catalog."
        );

        response.append("\n\n");

        // -----------------------------------------------------
        // Gender information
        // -----------------------------------------------------

        if (requestedGender == Gender.MEN) {

            response.append(
                    "I matched the services specifically "
                            + "for men."
            );

            response.append("\n\n");

        } else if (requestedGender == Gender.WOMEN) {

            response.append(
                    "I matched the services specifically "
                            + "for women."
            );

            response.append("\n\n");
        }

        boolean hasServices =
                serviceRecommendations != null
                        && !serviceRecommendations.isEmpty();

        boolean hasProducts =
                productRecommendations != null
                        && !productRecommendations.isEmpty();

        // -----------------------------------------------------
        // SERVICES
        // -----------------------------------------------------

        if (hasServices) {

            response.append(
                    "Recommended Services:"
            );

            response.append("\n");

            for (ServiceRecommendation recommendation
                    : serviceRecommendations) {

                SalonService service =
                        recommendation.getService();

                response.append("• ")
                        .append(
                                safe(
                                        service.getServiceName(),
                                        "Salon Service"
                                )
                        )
                        .append(" - ₹")
                        .append(
                                formatPrice(
                                        service.getPrice()
                                )
                        );

                if (service.getDuration() != null) {

                    response.append(" (")
                            .append(service.getDuration())
                            .append(" min)");
                }

                response.append("\n");
            }

            response.append("\n");
        }

        // -----------------------------------------------------
        // PRODUCTS
        // -----------------------------------------------------

        if (hasProducts) {

            response.append(
                    "Recommended Products:"
            );

            response.append("\n");

            for (ProductRecommendation recommendation
                    : productRecommendations) {

                ProductClass product =
                        recommendation.getProduct();

                response.append("• ")
                        .append(
                                safe(
                                        product.getProductName(),
                                        "Salon Product"
                                )
                        );

                if (product.getBrand() != null
                        && !product.getBrand().isBlank()) {

                    response.append(" by ")
                            .append(product.getBrand());
                }

                if (product.getRating() != null
                        && product.getReviewCount() != null
                        && product.getReviewCount() > 0) {

                    response.append(" - Rating: ")
                            .append(
                                    String.format(
                                            Locale.US,
                                            "%.1f",
                                            product.getRating()
                                    )
                            )
                            .append("/5");
                }

                if (product.getPrice() != null) {

                    response.append(" - ₹")
                            .append(
                                    formatPrice(
                                            product.getPrice()
                                    )
                            );
                }

                response.append("\n");
            }

            response.append("\n");
        }

        // -----------------------------------------------------
        // NOTHING FOUND
        // -----------------------------------------------------

        if (!hasServices && !hasProducts) {

            response.append(
                    buildNoMatchResponse(
                            userMessage,
                            intent
                    )
            );

            return response.toString();
        }

        // -----------------------------------------------------
        // FOOTER
        // -----------------------------------------------------

        response.append(
                "These recommendations are based only on "
                        + "SalonHub's current products and services."
        );

        return response.toString();
    }

    // =========================================================
    // NO MATCH RESPONSE
    // =========================================================

    private String buildNoMatchResponse(
            String userMessage,
            AIIntent intent) {

        String message =
                normalize(userMessage);

        if (intent == AIIntent.PRODUCT_CONCERN
                || isProductRelatedQuestion(message)) {

            return "I couldn't find a close product match "
                    + "for this concern in the current SalonHub "
                    + "catalog.\n"
                    + "Please describe the concern with a little "
                    + "more detail, such as your hair or skin type.";
        }

        if (isServiceIntent(intent)) {

            return "I couldn't find a close service match "
                    + "for this request in the current SalonHub "
                    + "catalog.\n"
                    + "Please describe what service you are "
                    + "looking for in a little more detail.";
        }

        return "I couldn't find a close match in the current "
                + "SalonHub catalog.\n"
                + "You can ask me about a haircut, hair treatment, "
                + "facial, grooming service, or SalonHub product.";
    }

    // =========================================================
    // HELPERS
    // =========================================================

    private boolean containsAny(
            String text,
            String... values) {

        if (text == null || values == null) {
            return false;
        }

        String normalizedText =
                normalize(text);

        for (String value : values) {

            if (value == null || value.isBlank()) {
                continue;
            }

            if (normalizedText.contains(
                    normalize(value))) {

                return true;
            }
        }

        return false;
    }

    private String normalize(String value) {

        if (value == null) {
            return "";
        }

        return value
                .toLowerCase(Locale.ROOT)
                .replaceAll("\\s+", " ")
                .trim();
    }

    private String safe(
            String value,
            String fallback) {

        if (value == null || value.isBlank()) {
            return fallback;
        }

        return value;
    }

    private String formatPrice(
            Double price) {

        if (price == null) {
            return "0";
        }

        if (price == Math.floor(price)) {
            return String.format(
                    Locale.US,
                    "%.0f",
                    price
            );
        }

        return String.format(
                Locale.US,
                "%.2f",
                price
        );
    }

    private Double getRating(
            ProductClass product) {

        if (product == null
                || product.getRating() == null) {

            return 0.0;
        }

        return product.getRating();
    }

    private Integer getReviewCount(
            ProductClass product) {

        if (product == null
                || product.getReviewCount() == null) {

            return 0;
        }

        return product.getReviewCount();
    }

    private Double getPrice(
            SalonService service) {

        if (service == null) {
            return null;
        }

        return service.getPrice();
    }

    private ProductGender convertGender(
            Gender gender) {

        if (gender == Gender.MEN) {
            return ProductGender.MEN;
        }

        if (gender == Gender.WOMEN) {
            return ProductGender.WOMEN;
        }

        return ProductGender.UNISEX;
    }

    // =========================================================
    // RECOMMENDATION HOLDER - PRODUCT
    // =========================================================

    private static class ProductRecommendation {

        private final ProductClass product;
        private final int score;

        public ProductRecommendation(
                ProductClass product,
                int score) {

            this.product = product;
            this.score = score;
        }

        public ProductClass getProduct() {
            return product;
        }

        public int getScore() {
            return score;
        }
    }

    // =========================================================
    // RECOMMENDATION HOLDER - SERVICE
    // =========================================================

    private static class ServiceRecommendation {

        private final SalonService service;
        private final int score;

        public ServiceRecommendation(
                SalonService service,
                int score) {

            this.service = service;
            this.score = score;
        }

        public SalonService getService() {
            return service;
        }

        public int getScore() {
            return score;
        }
    }

    // =========================================================
    // AI INTENT
    // =========================================================

    private enum AIIntent {

        HAIRCUT,

        HAIR_TREATMENT,

        HAIR_COLOR,

        FACIAL,

        MAKEUP,

        NAIL,

        BEARD_GROOMING,

        SPA,

        PRODUCT_CONCERN,

        GENERAL_SERVICE,

        UNKNOWN
    }

    // =========================================================
    // PRODUCT GENDER
    // =========================================================

    private enum ProductGender {

        MEN,

        WOMEN,

        UNISEX
    }
}