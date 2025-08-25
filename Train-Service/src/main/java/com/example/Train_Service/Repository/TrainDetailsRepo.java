package com.example.Train_Service.Repository;
import com.example.Train_Service.Entity.TrainDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TrainDetailsRepo extends JpaRepository<TrainDetails,Integer> {


    TrainDetails findByTrainNumber(int trainNumber);

    @Query(value = "select * from train_details r where r.train_name =:Name",nativeQuery = true)
    TrainDetails findbytrainname(@Param("Name") String trainname);


    List<TrainDetails> findAllByFromStationDepartureDate(LocalDate localDate);

    void deleteByTrainNumber(Integer trainNumber);


//    List<TrainDetails> findByStartingPointIgnoreCase(String startingpoint);

  /*  @Transactional
    @Modifying
    @Query(value = "update traindetails set no_of_stations = :Value where train_number = :train_number",nativeQuery = true)
    TrainDetails savenoofstaion_by_trainnumber(@Param("Value") int val,@Param("train_number") int trainnumber);*/

//    @Transactional
//    @Modifying
//    @Query(value = "update train_details set no_ac_tickets = ?2 where train_number = ?1",nativeQuery = true)
//    void updateTrainTicket(int trainnumber,int no_of_tickets);
}
