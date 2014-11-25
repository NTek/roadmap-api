package com.ramotion.roadmap.repository;

import com.ramotion.roadmap.model.VoteEntity;
import com.ramotion.roadmap.model.VoteEntityPK;
import org.springframework.data.repository.CrudRepository;

public interface VoteRepository extends CrudRepository<VoteEntity, VoteEntityPK> {


}
