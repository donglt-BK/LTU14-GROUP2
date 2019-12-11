package com.bk.olympia.model;

import java.time.LocalDateTime;

public class History {
    public void setRoomId(int[] roomId) {
        this.roomId = roomId;
    }

    public void setCreatedAt(LocalDateTime[] createdAt) {
        this.createdAt = createdAt;
    }

    public void setEndedAt(LocalDateTime[] endedAt) {
        this.endedAt = endedAt;
    }

    public void setResultType(String[] resultType) {
        this.resultType = resultType;
    }

    public void setBalanceChanged(int[] balanceChanged) {
        this.balanceChanged = balanceChanged;
    }

    private static History instance;
    public static History getInstance(){
        if(instance == null){
            instance = new History();
        }
        return instance;
    }

    private  int[] roomId;
    private  LocalDateTime[] createdAt;
    private  LocalDateTime[] endedAt;
    private  String[] resultType;
    private  int[] balanceChanged;

//    public History(int[] roomId, LocalDateTime[] createdAt, LocalDateTime[] endedAt, String[] resultType, int[] balanceChanged) {
//        this.roomId = roomId;
//        this.createdAt = createdAt;
//        this.endedAt = endedAt;
//        this.resultType = resultType;
//        this.balanceChanged = balanceChanged;
//    }


    public int[] getRoomId() {
        return roomId;
    }

    public LocalDateTime[] getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime[] getEndedAt() {
        return endedAt;
    }

    public String[] getResultType() {
        return resultType;
    }

    public int[] getBalanceChanged() {
        return balanceChanged;
    }
}
