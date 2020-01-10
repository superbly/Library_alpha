package com.example.library_alpha.module;

import com.example.library_alpha.readingRoom.SeatDto;
import com.example.library_alpha.user.UserDto;
import com.example.library_alpha.util.ReservationTimeAdd;
import com.example.library_alpha.util.TimeConvert;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import static com.example.library_alpha.readingRoom.ReadingRoom1.totalSeat;
import static com.example.library_alpha.LoginActivity.loginId;

public class Dao {

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();


    public void updateUser(int position, int roomNum, UserDto userDto, boolean reservationCheck, String reservationTime, String remainTime) {
        userDto.setSeatNum(position + 1);
        userDto.setRoomNum(roomNum);
        userDto.setReservationCheck(reservationCheck);
        userDto.setReservationDate(reservationTime);
        userDto.setRemainTime(remainTime);
        Map<String, Object> postValues = userDto.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/User/" + loginId, postValues);
        databaseReference.updateChildren(childUpdates);
    }


    public void updateUser(UserDto userDto) {
        userDto.setReservationCheck(false);
        userDto.setReservationDate("");
        userDto.setRemainTime("");
        userDto.setRoomNum(0);
        userDto.setSeatNum(0);
        Map<String, Object> postValues = userDto.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/User/" + loginId, postValues);
        databaseReference.updateChildren(childUpdates);
    }

    public void updateUser(UserDto userDto, String renewTime) {
        userDto.setRemainTime(renewTime);
        Map<String, Object> postValues = userDto.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/User/" + loginId, postValues);
        databaseReference.updateChildren(childUpdates);

    }


    public void updateSeat(int position, String reservationTime) {
        String key = databaseReference.child("Room").push().getKey();
        SeatDto seatDb = new SeatDto(position + 1, loginId, true, reservationTime);
        Map<String, Object> postValues = seatDb.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Room/" + (position + 1) + "seat", postValues);
        databaseReference.updateChildren(childUpdates);
    }


    public void emptySeat(int usedSeat) {
        String key = databaseReference.child("Room").push().getKey();
        SeatDto seatDb = new SeatDto(usedSeat, "", false, "");
        Map<String, Object> postValues = seatDb.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Room/" + (usedSeat) + "seat", postValues);
        databaseReference.updateChildren(childUpdates);
    }


    public void upCount() {
        SeatCount seatCount = new SeatCount(Integer.toString(totalSeat + 1));
        Map<String, Object> postValues = seatCount.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/seat_count/" + "nowSeatCount", postValues);
        databaseReference.updateChildren(childUpdates);
    }


    public void downCount() {
        SeatCount seatCount = new SeatCount(Integer.toString(totalSeat - 1));
        Map<String, Object> postValues = seatCount.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/seat_count/" + "nowSeatCount", postValues);
        databaseReference.updateChildren(childUpdates);
    }


}
