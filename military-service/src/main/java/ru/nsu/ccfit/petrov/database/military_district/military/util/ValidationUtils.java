package ru.nsu.ccfit.petrov.database.military_district.military.util;

import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class ValidationUtils {

    public static final String LAST_NAME_REGEX = "^[a-zA-Zа-яА-я]+$";
    public static final String FIRST_NAME_REGEX = "^[a-zA-Zа-яА-я]+$";
    public static final String MIDDLE_NAME_REGEX = "^[a-zA-Zа-яА-я]*$";
}
