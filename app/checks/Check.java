package checks;

import java.util.LinkedList;
import java.util.List;

import checks.unit.UnitCheck;
import play.data.validation.ValidationError;
/**
 * 入力チェッククラス
 * @author d_rabbit
 */
public abstract class Check {
	
	/**
	 * チェックケース登録
	 * @param checks
	 */
	abstract void entry(List<UnitCheck> checks);
	
	/**
	 * 全チェック実行
	 * @param errors
	 */
	protected void execute(List<ValidationError> errors){
		List<UnitCheck> checks = new LinkedList<>();
		entry(checks);
		/*
		 * エラーを検出するまで
		 * 登録したチェックを実行する。
		 */
		for(UnitCheck check: checks){
			if(check.execute()){
				errors.addAll(check.errList);
				break;
			}
		}
	}
	
}
