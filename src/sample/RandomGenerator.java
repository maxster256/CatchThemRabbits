package sample;

import java.util.Random;

class RandomGenerator extends Random {

    int generateValueInRange(int rangeFrom, int rangeTo) {
        return rangeFrom + nextInt(rangeTo - rangeFrom);
    }

}