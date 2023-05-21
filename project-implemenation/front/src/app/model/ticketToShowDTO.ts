import { Discount } from "./discount"

export interface TicketToShowDTO{
    discounts: Discount[]
    finalPrice: number
    alternativeFlightId: number
}