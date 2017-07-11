package hr.fer.zemris.java.hw04.db;

import java.util.List;

/**
 * Konkretna strategija sučelja {@link IFilter} koja se koristi za određivanje
 * zadovoljava li određeni primjerak razreda {@link StudentRecord} kriterije
 * predane kroz konstruktor ovog razreda {@link #QueryFilter(List)}. Razred
 * implementira metodu {@link #accepts(StudentRecord)} koja pomoću predane liste
 * ispituje kriterije koje primjerak razreda {@link StudentRecord} mora
 * zadovoljiti
 * 
 * @see StudentRecord
 * @see IFilter
 * @see ConditionalExpression
 * 
 * @author Davor Češljaš
 */
public class QueryFilter implements IFilter {

	/**
	 * Lista svih kriterija koji moraju biti zadovoljeni za pojedini primjerak
	 * razreda {@link StudentRecord}
	 */
	private List<ConditionalExpression> conditionalExpressions;

	/**
	 * Konstruktor koji inicijalizira primjerak ovog razreda sa predanom listom
	 * <b>conditionalExpressions</b>
	 *
	 * @param conditionalExpressions
	 *            Lista svih kriterija koji moraju biti zadovoljeni za pojedini
	 *            primjerak razreda {@link StudentRecord}
	 */
	public QueryFilter(List<ConditionalExpression> conditionalExpressions) {
		this.conditionalExpressions = conditionalExpressions;
	}

	@Override
	public boolean accepts(StudentRecord record) {
		for (ConditionalExpression conditionalExpression : conditionalExpressions) {
			String recordFieldValue = conditionalExpression.getFieldGetter().get(record);
			if (!conditionalExpression.getComparisonOperator().satisfied(recordFieldValue,
					conditionalExpression.getStringLiteral())) {
				return false;
			}
		}
		// ako niti jedan ne vrati false onda vrati true
		return true;
	}

}
