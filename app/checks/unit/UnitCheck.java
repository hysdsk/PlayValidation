package checks.unit;

import java.util.ArrayList;
import java.util.List;

import play.data.validation.ValidationError;
/**
 * ケース単位チェッククラス
 * @author d_rabbit
 */
public abstract class UnitCheck {
	/** エラーリスト */
	public List<ValidationError> errList;
	/**
	 * 検査ロジック
	 * @return 判定結果
	 */
	public abstract boolean valid();
	/**
	 * エラー情報出力
	 * @param errList エラーリスト
	 */
	public abstract void outputErr(List<ValidationError> errList);
	/**
	 * 処理実行
	 * @return 判定結果
	 */
	public boolean execute(){
		boolean result = valid();
		if(result) {
			errList = new ArrayList<ValidationError>();
			outputErr(errList);
		}
		return result;
	}
	
}
