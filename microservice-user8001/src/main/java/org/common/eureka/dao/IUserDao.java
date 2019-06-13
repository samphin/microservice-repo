package org.common.eureka.dao;

import org.common.eureka.entity.SecUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserDao extends JpaRepository<SecUser,String> {
	
	@Query("from SecUser where userName like:userName")  
    public SecUser findUserByName(@Param("userName") String userName);
	
}
