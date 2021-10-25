package com.uni.spectro.domain.calculations

import com.uni.spectro.constants.SpectrumConstants.PIXEL_COUNT
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.util.*

class SensorCalibration {

    fun nanometerFor(pixelIndex: Int): Int {
        val exactNanometer = exactNanometerForPixel(pixelIndex)
        return exactNanometer!!.toInt()
    }

    private fun exactNanometerForPixel(pixelIndex: Int): BigDecimal? {
        if (CALIBRATION_TABLE.containsKey(pixelIndex)) {
            return CALIBRATION_TABLE[pixelIndex]
        }
        val nanometer = A0.add(bCoefficients(pixelIndex))
        CALIBRATION_TABLE[pixelIndex] = nanometer
        return nanometer
    }

    private fun bCoefficients(pixelIndex: Int): BigDecimal {
        val pixel = BigDecimal(pixelIndex)
        val b1 = B1.multiply(pixel)
        val b2 = B2.multiply(pixel.pow(2))
        val b3 = B3.multiply(pixel.pow(3))
        val b4 = B4.multiply(pixel.pow(4))
        val b5 = B5.multiply(pixel.pow(5))
        return listOf<BigDecimal>(b1, b2, b3, b4, b5).fold(ZERO, { acc, next -> acc.add(next) })
    }

    fun visibleRange(): List<Int> {
        return listOf(400, 404, 406, 409, 411, 414, 416, 419, 422, 424, 427, 429, 432, 434,
                437, 439, 442, 444, 447, 449, 452, 455, 457, 460, 462, 464, 467, 469, 472, 474, 477,
                479, 482, 484, 487, 489, 492, 494, 496, 499, 501, 504, 506, 508, 511, 513, 516, 518,
                520, 523, 525, 528, 530, 532, 535, 537, 539, 542, 544, 546, 548, 551, 553, 555, 558,
                560, 562, 565, 567, 569, 571, 574, 576, 578, 580, 582, 585, 587, 589, 591, 594, 596,
                598, 600, 602, 604, 607, 609, 611, 613, 615, 617, 619, 622, 624, 626, 628, 630, 632,
                634, 636, 638, 640, 642, 644, 647, 649, 651, 653, 655, 657, 659, 661, 663, 665, 667,
                669, 671, 673, 674, 676, 678, 680, 682, 684, 686, 688, 690, 692, 694, 696, 697, 699,
                701, 703, 705, 707, 709, 710, 712, 714, 716, 718, 720, 721, 723, 725, 727, 728, 730,
                732, 734, 735, 737, 739, 741, 742, 744, 746, 747, 749, 751, 753, 754, 756, 758, 759,
                761, 763, 764, 766, 767, 769, 771, 772, 774, 775, 777, 779, 780, 782, 783, 785, 786,
                788, 790, 791, 793, 794, 796, 797, 799, 800)
    }

    fun fullRange(): List<Int> {
        return listOf(316, 319, 322, 324, 327, 330, 332, 335, 338, 340, 343, 346, 348, 351, 354, 356, 359, 362,
                364, 367, 370, 372, 375, 378, 380, 383, 385, 388, 391, 393, 396, 398, 401, 404, 406, 409, 411, 414, 416,
                419, 422, 424, 427, 429, 432, 434, 437, 439, 442, 444, 447, 449, 452, 455, 457, 460, 462, 464, 467, 469,
                472, 474, 477, 479, 482, 484, 487, 489, 492, 494, 496, 499, 501, 504, 506, 508, 511, 513, 516, 518, 520,
                523, 525, 528, 530, 532, 535, 537, 539, 542, 544, 546, 548, 551, 553, 555, 558, 560, 562, 565, 567, 569,
                571, 574, 576, 578, 580, 582, 585, 587, 589, 591, 594, 596, 598, 600, 602, 604, 607, 609, 611, 613, 615,
                617, 619, 622, 624, 626, 628, 630, 632, 634, 636, 638, 640, 642, 644, 647, 649, 651, 653, 655, 657, 659,
                661, 663, 665, 667, 669, 671, 673, 674, 676, 678, 680, 682, 684, 686, 688, 690, 692, 694, 696, 697, 699,
                701, 703, 705, 707, 709, 710, 712, 714, 716, 718, 720, 721, 723, 725, 727, 728, 730, 732, 734, 735, 737,
                739, 741, 742, 744, 746, 747, 749, 751, 753, 754, 756, 758, 759, 761, 763, 764, 766, 767, 769, 771, 772,
                774, 775, 777, 779, 780, 782, 783, 785, 786, 788, 790, 791, 793, 794, 796, 797, 799, 800, 802, 803, 805,
                806, 808, 809, 811, 812, 813, 815, 816, 818, 819, 821, 822, 823, 825, 826, 828, 829, 830, 832, 833, 835,
                836, 837, 839, 840, 841, 843, 844, 845, 847, 848, 849, 851, 852, 853, 854, 856, 857, 858, 860, 861, 862,
                863, 865, 866, 867, 868, 870, 871, 872, 873, 875, 876, 877, 878, 879, 881, 882, 883, 884)
    }

    fun indexForWavelength(wavelength: Int): Int {
        return Collections.binarySearch(fullRange(), wavelength)
    }

    companion object {
        private val A0 = BigDecimal("3.141620094E+02")
        private val B1 = BigDecimal("2.694245852E+00")
        private val B2 = BigDecimal("-1.236874166E-03")
        private val B3 = BigDecimal("-6.938527672E-06")
        private val B4 = BigDecimal("6.528119809E-09")
        private val B5 = BigDecimal("9.085059701E-12")
        private val CALIBRATION_TABLE: MutableMap<Int, BigDecimal> = HashMap<Int, BigDecimal>(PIXEL_COUNT)
    }
}