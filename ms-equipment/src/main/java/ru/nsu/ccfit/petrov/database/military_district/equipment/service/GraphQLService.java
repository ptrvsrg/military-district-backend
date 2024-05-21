package ru.nsu.ccfit.petrov.database.military_district.equipment.service;

import java.util.Map;
import lombok.NonNull;

public interface GraphQLService {

  Object resolveReference(@NonNull Map<String, Object> reference);
}
