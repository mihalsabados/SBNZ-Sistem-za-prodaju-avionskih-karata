import { Discount } from "./discount"

export interface TicketToShowDTO{
    discounts: Discount[]
    basePrice: number
    finalPrice: number
    alternativeFlightId: number
    flightFound: boolean
}