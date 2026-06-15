package com.jedi.jedis.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jedi.jedis.model.Jedi;

public interface JediRepository extends JpaRepository<Jedi,Integer>{

}
