package com.prs.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.prs.api.entity.Subscription;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long>{

	@Query(value = "SELECT * FROM subscription WHERE sub_id=:subId  and enabled = true", nativeQuery = true)
	Optional<Subscription> findSubscriptionActiveById(Long subId);

}
