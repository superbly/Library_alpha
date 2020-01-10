package com.example.library_alpha.readingRoom;

import com.example.library_alpha.module.Dao;
import com.example.library_alpha.user.UserDto;
import com.example.library_alpha.util.ReservationTimeAdd;

import java.util.List;

public class Function { // 사용자의 예약, 반납, 연장 기능을 담당하는 클래스

    Dao dao = new Dao();

    public void reservationSeat(int position, int roomNum, UserDto userDto, List<SeatDto> seatDto, String reservationTime) {


        ReservationTimeAdd reservationTimeAdd = new ReservationTimeAdd();

        // 열람실 좌석수 1 증가
        dao.upCount();

        // 좌석 정보 업데이트
        dao.updateSeat(position, reservationTimeAdd.add(reservationTime));

        // 유저 정보 업데이트
        dao.updateUser(position, roomNum, userDto, true, reservationTime, reservationTimeAdd.add(reservationTime));

        // 예약 좌석의 색상을 붉은색으로 변경
        seatDto.get(position).setSeatCheck(true);
    }


    public void moveSeat(int position, int roomNum, UserDto userDto, List<SeatDto> seatDto) {

        // 이전 좌석의 색상을 원래로 되돌림
        seatDto.get((userDto.getSeatNum()) - 1).setSeatCheck(false);

        // 이동할 좌석의 색상을 붉은색으로 변경
        seatDto.get(position).setSeatCheck(true);

        // 전에 사용하던 좌석에서 사용자 정보 제거
        dao.emptySeat(userDto.getSeatNum());

        // 해당 유저 좌석 열람실 번호와 좌석번호 변경
        dao.updateUser(position, roomNum, userDto, true, userDto.getReservationDate(), userDto.getRemainTime());

        // 이동할 좌석에 사용자 정보 업데이트
        dao.updateSeat(position, userDto.getRemainTime());
    }


    public void returnSeat(UserDto userDto) {

        // 열람실 좌석수 1 감소
        dao.downCount();

        // 좌석정보에서 사용자 정보 제거
        dao.emptySeat(userDto.getSeatNum());

        // 사용자 정보에서 좌석 정보 제거
        dao.updateUser(userDto);

    }


    public void renew(UserDto userDto) {
        ReservationTimeAdd reservationTimeAdd = new ReservationTimeAdd();
        String result = reservationTimeAdd.renew(userDto.getRemainTime());
        dao.updateUser(userDto, result);
    }

}
