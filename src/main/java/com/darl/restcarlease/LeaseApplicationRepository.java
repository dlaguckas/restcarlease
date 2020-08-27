package com.darl.restcarlease;

import org.springframework.data.jpa.repository.JpaRepository;

interface LeaseApplicationRepository extends JpaRepository<LeaseApplication, Long> {

}