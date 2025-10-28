package com.springboot.cruddemo.util;

import com.springboot.cruddemo.exception.BadRequestException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
public class PatchValidationUtil {

    private static final Set<String> ALLOWED_PATCH_FIELDS = Set.of(
            "firstName",
            "lastName",
            "email"
    );

    public void validatePatchFields(Map<String, Object> patchPayload) {
        if (patchPayload == null || patchPayload.isEmpty()) {
            throw new BadRequestException("Patch payload cannot be null or empty");
        }

        // Validate that no "id" field is provided
        if (patchPayload.containsKey("id")) {
            throw new BadRequestException("ID field is not allowed in patch request");
        }

        // Validate that only allowed fields are provided
        for (String field : patchPayload.keySet()) {
            if (!ALLOWED_PATCH_FIELDS.contains(field)) {
                throw new BadRequestException("Field '" + field + "' is not allowed for patching. " +
                        "Allowed fields: " + ALLOWED_PATCH_FIELDS);
            }
        }
    }

    public void validateFieldValues(Map<String, Object> patchPayload) {
        for (Map.Entry<String, Object> entry : patchPayload.entrySet()) {
            String field = entry.getKey();
            Object value = entry.getValue();

            // Basic validation for string fields
            if (ALLOWED_PATCH_FIELDS.contains(field)) {
                if (value != null && !(value instanceof String)) {
                    throw new BadRequestException("Field '" + field + "' must be a string value");
                }

                if (value instanceof String && ((String) value).trim().isEmpty()) {
                    throw new BadRequestException("Field '" + field + "' cannot be empty");
                }
            }
        }
    }
}
