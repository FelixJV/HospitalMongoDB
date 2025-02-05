package org.example.dao.repositories;


import org.example.dao.model.Credential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CredentialRepository extends JpaRepository<Credential, Integer> {

    Credential findByUsername(String username);
}
