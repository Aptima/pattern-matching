package com.aptima.netstorm.algorithms.aptima.bp.mismatch;

public class RangeProcessor extends DefaultAttributeMismatchProcessor {

	public static String RANGE = "RANGE";

	// splits the input by "operand" (e.g. >=)
	// then, based on the whether the operand uses equality or is 'flipped' (>)
	// populates the objects low and high seen, equal, and value
	// these values are then used to evaluate the data value itself against the range
	/**splits the input by "operand" 
	then, based on the whether the operand uses equality or is 'flipped' (>)
	 populates the objects low and high seen, equal, and value
	these values are then used to evaluate the data value itself against the range
	 * 
	 * @param operand					String operation - (e.g. >=)
	 * @param input						The operation that needs to be processed					
	 * @param lowValue					If it's "flipped" the lowValue represents the lower number in a>b (b in this case)
	 * @param highValue					If it's "flipped" the lowValue represents the higher number in a>b (a in this case)
	 * @param lowSeen					Boolean that indicates a low was set
	 * @param lowEqual					Boolean that indicates a low was set, but the operand is equal
	 * @param highSeen					Boolean that indicates a high was set
	 * @param highEqual					Boolean that indicates a high was set, but the operand is equal
	 * @param rangeSeen					Boolean that verifies you hit the end with ""		
	 * @return							Boolean that is true if comparison took place and false if not		
	 */
	private static boolean processSplit(String operand, String input, WrapDouble lowValue, WrapDouble highValue,
			WrapBoolean lowSeen, WrapBoolean lowEqual, WrapBoolean highSeen, WrapBoolean highEqual, WrapBoolean rangeSeen) {
		if (input.contains(operand)) {
			boolean equal = operand.contains("=");
			boolean flipped = operand.contains(">");
			String[] splitInToOperands = input.split(operand);
			for (int j = 0; j < splitInToOperands.length; j++) {
				splitInToOperands[j] = splitInToOperands[j].trim();
				if (splitInToOperands[j].equals("")) {
					rangeSeen.set(true); // whether RANGE comes first or last (determines low value or high value
											// population)
				} else {
					if (!rangeSeen.get()) {
						if (!flipped) {
							lowValue.set(Double.parseDouble(splitInToOperands[j]));
							if (equal) {
								lowEqual.set(true);
							} else {
								lowEqual.set(false);
							}
							lowSeen.set(true);
						} else { // in flipped, everything is reversed
							highValue.set(Double.parseDouble(splitInToOperands[j]));
							if (equal) {
								highEqual.set(true);
							} else {
								highEqual.set(false);
							}
							highSeen.set(true);
						}
					} else {
						if (!flipped) {
							highValue.set(Double.parseDouble(splitInToOperands[j]));
							if (equal) {
								highEqual.set(true);
							} else {
								highEqual.set(false);
							}
							highSeen.set(true);
						} else {
							lowValue.set(Double.parseDouble(splitInToOperands[j]));
							if (equal) {
								lowEqual.set(true);
							} else {
								lowEqual.set(false);
							}
							lowSeen.set(true);
						}
					}
				}
			}
			return true;
		}
		return false;
	}
	
	/**Method gets the mismatch between a two strings of a model and data element
	 * 
	 * @param modelValue			String that holds the value of the model to compare
	 * @param dataValue				String that holds the value of the data to compare
	 * 
	 * @return						double mismatch between the two strings
	 */
	public double getMismatch(String modelValue, String dataValue)
	{
		double mm = super.processSimpleCases(modelValue, dataValue);
		
		if (mm >= 0.0)
			return mm;
		
		boolean rangeUsed = modelValue.contains(RangeProcessor.RANGE);
		
		if (!rangeUsed) {
			// process the model vs. data value in equality case	
			Double dModelValue = Double.parseDouble(modelValue);
			Double dDataValue = Double.parseDouble(dataValue);
			
			if (dModelValue.equals(dDataValue)) { // perfect match
				return 0.0;
			} else {
				Double diff = Math.abs(dDataValue - dModelValue);
				Double norm = 4 * Math.abs(dModelValue / 10.0);
				return Math.min(diff / norm, 1);
			}
		}

		// do we need to build a full blown interpreter?
		// Currently understands things like
		// 10 <= RANGE
		// 10 < RANGE
		// RANGE <= 10
		// RANGE < 10
		// 10 <= RANGE <= 100
		// 10 < RANGE < 100
		// 10 <= RANGE < 100
		// 10 < RANGE <= 100
		// as well as inverses of the above and operand flips
		// 100 >= RANGE >= 10

		// populate terms
		// java does not have pass primitives by reference, so I'm using
		// objects to preserve the values through a processing subroutine
		// (the standard wrappers don't have 'sets' for this)
		WrapDouble lowValue = new WrapDouble();
		WrapDouble highValue = new WrapDouble();
		WrapBoolean lowSeen = new WrapBoolean();
		WrapBoolean lowEqual = new WrapBoolean();
		WrapBoolean highSeen = new WrapBoolean();
		WrapBoolean highEqual = new WrapBoolean();
		WrapBoolean rangeSeen = new WrapBoolean();

		String[] splitOverRange = modelValue.split(RANGE);
		for (int i = 0; i < splitOverRange.length; i++) {
			splitOverRange[i] = splitOverRange[i].trim();
			if (splitOverRange[i].equals("")) {
				rangeSeen.set(true); // RANGE here helps determine the order of the operands
			} else {
				// parse all the operands
				boolean lessThan = processSplit("<=", splitOverRange[i], lowValue, highValue, lowSeen, lowEqual, highSeen,
						highEqual, rangeSeen);
				if (!lessThan) {
					lessThan = processSplit("=<", splitOverRange[i], lowValue, highValue, lowSeen, lowEqual, highSeen, highEqual,
							rangeSeen);
				}
				if (!lessThan) {
					processSplit("<", splitOverRange[i], lowValue, highValue, lowSeen, lowEqual, highSeen, highEqual, rangeSeen);
				}
				boolean greaterThan = processSplit(">=", splitOverRange[i], lowValue, highValue, lowSeen, lowEqual, highSeen,
						highEqual, rangeSeen);
				if (!greaterThan) {
					greaterThan = processSplit("=>", splitOverRange[i], lowValue, highValue, lowSeen, lowEqual, highSeen,
							highEqual, rangeSeen);
				}
				if (!greaterThan) {
					processSplit(">", splitOverRange[i], lowValue, highValue, lowSeen, lowEqual, highSeen, highEqual, rangeSeen);
				}
			}
		}
		
		Double min = lowSeen.get() ? lowValue.get() : -1 * Integer.MAX_VALUE;
		Double max = highSeen.get() ? highValue.get() : Integer.MAX_VALUE;
		
		Double minNotEqualPenalty = lowEqual.get() ? 0 : 0.0001;
		Double maxNotEqualPenalty = highEqual.get() ? 0 : 0.0001;
		
		// process the value vs. the range;
		Double dDataValue = Double.parseDouble(dataValue);
		
		if(dDataValue > min && dDataValue < max) {
			return 0;
		}

		if (dDataValue <= min && Math.signum(min) == Math.signum(dDataValue)) {
			return Math.min(1.0, Math.abs((min - dDataValue) / min) + minNotEqualPenalty);
		}
		
		if (dDataValue >= max && Math.signum(max) == Math.signum(dDataValue)) {
			return Math.min(1.0, Math.abs((max - dDataValue) / max) + maxNotEqualPenalty);
		}
		
		return 1.0;
	}
	
	/**
	 * Returns a value between 0 and 1.  0 = perfect match, 1 = maximum mismatch.
	 * 
	 * @param modelVals					Array of model values
	 * @param dataVals					Array of data values
	 * @param index						Index of interest within arrays to compare
	 * @param rangeUsed					Unused boolean
	 * @return							double that is represents the mismatch
	 */
	public static double getPercentageMismatchedFinancialTransfer(String[] modelVals, String[] dataVals, int index,
			boolean rangeUsed) {

		//System.out.println("Model val: " + modelVals[index]);
		//System.out.println("Data val: " + dataVals[index]);
		
		String modelValue = modelVals[index];
		String dataValue = dataVals[index];
		
		return (new RangeProcessor()).getMismatch(modelValue, dataValue);
	}

	public static void main(String[] args) throws Exception {
		test();
	}

	public static void test() {

		// 10 <= RANGE
		// 10 < RANGE
		// RANGE <= 10
		// RANGE < 10
		// 10 <= RANGE <= 100
		// 10 < RANGE < 100
		// 10 <= RANGE < 100
		// 10 < RANGE <= 100

		String[] modelVals = new String[1];
		String[] dataVals = new String[1];
		
		double maxComparableMismatch = 10;

		modelVals[0] = "19700";
		System.out.println(modelVals[0]);
		dataVals[0] = "19800";
		System.out.println("("+dataVals[0]+") " + getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, false));
		dataVals[0] = "10200";
		System.out.println("("+dataVals[0]+") " + getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, false));
		dataVals[0] = "10000";
		System.out.println("("+dataVals[0]+") " + getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, false));
		dataVals[0] = "1200";
		System.out.println("("+dataVals[0]+") " + getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, false));
		dataVals[0] = "1100";
		System.out.println("("+dataVals[0]+") " + getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, false));
		
		System.out.println("");
		
		modelVals[0] = "10000";
		System.out.println(modelVals[0]);
		dataVals[0] = "9800";
		System.out.println("("+dataVals[0]+") " + getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, false) * maxComparableMismatch / 2.0);
		dataVals[0] = "10000";
		System.out.println("("+dataVals[0]+") " + getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, false)* maxComparableMismatch / 2.0);
		dataVals[0] = "10200";
		System.out.println("("+dataVals[0]+") " + getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, false)* maxComparableMismatch / 2.0);

		System.out.println("");
		
		modelVals[0] = "900 < RANGE < 1100";
		System.out.println(modelVals[0]);
		dataVals[0] = "800";
		System.out.println("("+dataVals[0]+") " + getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true)* maxComparableMismatch / 2.0);
		dataVals[0] = "900";
		double fVal = getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true) * maxComparableMismatch / 2.0;
		System.out.println("Financial: " + fVal);
		
		StringBuilder test = new StringBuilder();
		float hi = 0.0f;
		double val = hi + getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true) * maxComparableMismatch / 2.0;
		test.append(val);
		System.out.println(test.toString());

		dataVals[0] = "1000";
		System.out.println("("+dataVals[0]+") " + getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true)* maxComparableMismatch / 2.0);
		dataVals[0] = "1100";
		System.out.println("("+dataVals[0]+") " + getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true)* maxComparableMismatch / 2.0);
		dataVals[0] = "1200";
		System.out.println("("+dataVals[0]+") " + getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true)* maxComparableMismatch / 2.0);
		
		System.out.println("");
		
		modelVals[0] = "1 < RANGE < 2000";
		System.out.println(modelVals[0]);
		dataVals[0] = "1000";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
		dataVals[0] = "500";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
		dataVals[0] = "400";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
		dataVals[0] = "200";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
		dataVals[0] = "100";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
		dataVals[0] = "1100";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
		dataVals[0] = "1200";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));

		System.out.println("");
		
		modelVals[0] = "10";
		System.out.println(modelVals[0]);
		dataVals[0] = "10";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, false));
		dataVals[0] = "11";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, false));
		dataVals[0] = "12";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, false));
		dataVals[0] = "13";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, false));
		dataVals[0] = "14";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, false));
		dataVals[0] = "15";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, false));
		dataVals[0] = "9";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, false));
		dataVals[0] = "6";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, false));
		
		System.out.println("");
		
		modelVals[0] = "10 <= RANGE";
		System.out.println(modelVals[0]);
		dataVals[0] = "20";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
		dataVals[0] = "10";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
		dataVals[0] = "5";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));

//		modelVals[0] = "10<=RANGE";
//		dataVals[0] = "20";
//		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
//		dataVals[0] = "10";
//		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
//		dataVals[0] = "5";
//		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
//
//		modelVals[0] = " 10 <= RANGE ";
//		dataVals[0] = "20";
//		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
//		dataVals[0] = "10";
//		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
//		dataVals[0] = "5";
//		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));

//		modelVals[0] = "RANGE >= 10";
//		dataVals[0] = "20";
//		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
//		dataVals[0] = "10";
//		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
//		dataVals[0] = "5";
//		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
//
//		modelVals[0] = "RANGE => 10";
//		dataVals[0] = "20";
//		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
//		dataVals[0] = "10";
//		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
//		dataVals[0] = "5";
//		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));

		modelVals[0] = "10 < RANGE";
		System.out.println(modelVals[0]);
		dataVals[0] = "20";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
		dataVals[0] = "10";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
		dataVals[0] = "5";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));

//		modelVals[0] = "RANGE > 10";
//		dataVals[0] = "20";
//		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
//		dataVals[0] = "10";
//		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
//		dataVals[0] = "5";
//		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));

		System.out.println("");
		
		modelVals[0] = "RANGE <= 10";
		System.out.println(modelVals[0]);
		dataVals[0] = "5";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
		dataVals[0] = "10";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
		dataVals[0] = "20";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
		dataVals[0] = "40";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
		
//		modelVals[0] = "RANGE < 10";
//		dataVals[0] = "5";
//		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
//		dataVals[0] = "10";
//		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
//		dataVals[0] = "20";
//		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));

		System.out.println("");
		
		modelVals[0] = "10 <= RANGE <= 100";
		System.out.println(modelVals[0]);
		dataVals[0] = "5";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
		dataVals[0] = "10";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
		dataVals[0] = "15";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
		dataVals[0] = "100";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
		dataVals[0] = "200";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));

		System.out.println("");
		
		modelVals[0] = "10 < RANGE < 100";
		System.out.println(modelVals[0]);
		dataVals[0] = "5";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
		dataVals[0] = "10";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
		dataVals[0] = "15";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
		dataVals[0] = "100";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
		dataVals[0] = "200";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));

		System.out.println("");
		
		modelVals[0] = "10 <= RANGE < 100";
		System.out.println(modelVals[0]);
		dataVals[0] = "5";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
		dataVals[0] = "10";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
		dataVals[0] = "15";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
		dataVals[0] = "100";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
		dataVals[0] = "200";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));

		System.out.println("");
		
		modelVals[0] = "10 < RANGE <= 100";
		System.out.println(modelVals[0]);
		dataVals[0] = "5";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
		dataVals[0] = "10";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
		dataVals[0] = "15";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
		dataVals[0] = "100";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
		dataVals[0] = "200";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));

		System.out.println("");
		
		modelVals[0] = "100 >= RANGE >= 10";
		System.out.println(modelVals[0]);
		dataVals[0] = "5";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
		dataVals[0] = "10";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
		dataVals[0] = "15";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
		dataVals[0] = "100";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
		dataVals[0] = "200";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));

		System.out.println("");
		
		modelVals[0] = "100 >= RANGE > 10";
		System.out.println(modelVals[0]);
		dataVals[0] = "5";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
		dataVals[0] = "10";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
		dataVals[0] = "15";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
		dataVals[0] = "100";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
		dataVals[0] = "200";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));

		System.out.println("");
		
		modelVals[0] = "100 > RANGE >= 10";
		System.out.println(modelVals[0]);
		dataVals[0] = "5";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
		dataVals[0] = "10";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
		dataVals[0] = "15";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
		dataVals[0] = "100";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
		dataVals[0] = "200";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));

		System.out.println("");
		
		modelVals[0] = "100 > RANGE > 10";
		System.out.println(modelVals[0]);
		dataVals[0] = "5";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
		dataVals[0] = "10";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
		dataVals[0] = "15";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
		dataVals[0] = "100";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
		dataVals[0] = "200";
		System.out.println(getPercentageMismatchedFinancialTransfer(modelVals, dataVals, 0, true));
	}
}