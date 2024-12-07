package org.example.crudkudago.util;

import jakarta.persistence.criteria.Predicate;
import lombok.experimental.UtilityClass;
import org.example.crudkudago.entity.Event;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;


@UtilityClass
public class EventSpecification {
    public Specification<Event> filterEvents(String name, String place, LocalDate fromDate, LocalDate toDate) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (name != null && !name.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("name"), "%" + name + "%"));
            }
            if (place != null && !place.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("place"), "%" + place + "%"));
            }
            if (fromDate != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("fromDate"), fromDate));
            }
            if (toDate != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get("toDate"), toDate));
            }
            return predicate;
        };
    }
}
