package com.ftn.sbnz.enums;

import java.util.Random;

public enum TicketType {
    ECONOMIC, BUSINESS;

    private static final Random PRNG = new Random();

    public static TicketType randomTicketType()  {
        TicketType[] ticketTypes = values();
        return ticketTypes[PRNG.nextInt(ticketTypes.length)];
    }
}
