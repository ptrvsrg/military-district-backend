package ru.nsu.ccfit.petrov.database.military_district.weapon.service;

import java.util.Map;
import lombok.NonNull;

public interface GraphQLService {

  Object resolveReference(@NonNull Map<String, Object> reference);
}
