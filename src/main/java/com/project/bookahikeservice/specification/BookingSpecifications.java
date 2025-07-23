package com.project.bookahikeservice.specification;

import com.project.bookahikeservice.dto.response.BookingFilter;
import com.project.bookahikeservice.entity.Booking;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;


public class BookingSpecifications {
    public static Specification<Booking> withFilters(BookingFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getEventId() != null) {
                predicates.add(cb.equal(root.get("event").get("id"), filter.getEventId()));
            }
            if (filter.getBookingType() != null) {
                predicates.add(cb.equal(root.get("bookingType"), filter.getBookingType()));
            }
            if (filter.getJoinerId() != null) {
                predicates.add(cb.equal(root.get("joiner").get("id"), filter.getJoinerId()));
            }
            if (filter.getIsActive() != null) {
                predicates.add(cb.equal(root.get("isActive"), filter.getIsActive()));
            }
            if (filter.getIsCancelled() != null) {
                predicates.add(cb.equal(root.get("isCancelled"), filter.getIsCancelled()));
            }
            if (filter.getIsDone() != null) {
                predicates.add(cb.equal(root.get("isDone"), filter.getIsDone()));
            }
            if (filter.getCreatedAfter() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), filter.getCreatedAfter()));
            }
            if (filter.getUpdatedAfter() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("updatedAt"), filter.getUpdatedAfter()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
