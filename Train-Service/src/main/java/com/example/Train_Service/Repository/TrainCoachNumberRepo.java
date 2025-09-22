package com.example.Train_Service.Repository;

import com.example.Train_Service.Entity.TrainCoachNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainCoachNumberRepo extends JpaRepository<TrainCoachNumber,Integer> {
}
