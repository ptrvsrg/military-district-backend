package ru.nsu.ccfit.petrov.database.military_district.report.util;

import lombok.experimental.UtilityClass;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.function.Supplier;

@UtilityClass
public class DBUtil {

    public static <T> T getNullableResult(Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }
}
