package taxipark

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> =
    allDrivers - trips.map { it.driver }.toSet()

/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> =
    allPassengers.filter { p -> trips.count { p in it.passengers } >= minTrips }.toSet()

/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> =
    allPassengers.filter { p -> trips.count { it.driver == driver && p in it.passengers } > 1 }.toSet()

/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> =
    allPassengers.filter { p ->
        val withDiscount = trips.count { p in it.passengers && it.discount != null }
        val withoutDiscount = trips.count { p in it.passengers && it.discount == null }
        withDiscount > withoutDiscount
    }.toSet()

/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
    if (trips.isEmpty()) return null
    val bucket = trips.groupingBy { it.duration / 10 }.eachCount()
    val maxBucket = bucket.maxBy { it.value }.key
    return (maxBucket * 10)..(maxBucket * 10 + 9)
}

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {
    if (trips.isEmpty()) return false
    val totalIncome = trips.sumOf { it.cost }
    val top20Count = (allDrivers.size * 0.2).toInt()
    val top20Income = allDrivers
        .map { d -> trips.filter { it.driver == d }.sumOf { it.cost } }
        .sortedDescending()
        .take(top20Count)
        .sum()
    return top20Income >= totalIncome * 0.8
}