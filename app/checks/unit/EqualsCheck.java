package checks.unit;

/**
 * 同一文字列チェック
 * @author d_rabbit
 */
public abstract class EqualsCheck extends UnitCheck {
	String strA;
	String strB;
	public EqualsCheck(String strA, String strB){
		this.strA = strA;
		this.strB = strB;
	}
	
	@Override
	public boolean valid() {
		return !strA.equals(strB);
	}

}
