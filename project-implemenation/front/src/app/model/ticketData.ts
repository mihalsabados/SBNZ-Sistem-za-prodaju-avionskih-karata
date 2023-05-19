import { PassengerData } from "./passengerData";

export interface TicketData{
    passengerData: PassengerData
    payerEmail: string
    flightId: number
    cardType: string
}