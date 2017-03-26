package checks;

import java.util.List;

import checks.unit.UnitCheck;
import checks.unit.EqualsCheck;
import checks.unit.ManagerLimitCheck;
import forms.IndexForm;
import play.data.validation.ValidationError;

/**
 * フォームと対になるチェッククラス
 * @author d_rabbit
 */
public class IndexCheck extends Check {
	
	/** フォーム */
	IndexForm form;
	
	/**
	 * 全チェック実行
	 * @param form フォーム
	 * @param errors エラー情報
	 */
	public void execute(IndexForm form, List<ValidationError> errors){
		this.form = form;
		super.execute(errors);
	}
	
	@Override
	void entry(List<UnitCheck> checks) {
		/*
		 * パスワードと再入力パスワードの比較チェック
		 */
		checks.add(new EqualsCheck(form.password, form.againPassword){
			@Override
			public void outputErr(List<ValidationError> errList) {
				errList.add(new ValidationError("password", "再入力パスワードと一致しません"));
				errList.add(new ValidationError("againPassword", "パスワードと一致しません"));
			}
		});
		/*
		 * マネージャー年齢チェック
		 */
		checks.add(new ManagerLimitCheck(form.job, form.age, 30){
			@Override
			public void outputErr(List<ValidationError> errList) {
				errList.add(new ValidationError("job", limit + "歳未満の方はマネージャーを選択できません"));
			}
		});
		
		/*
		 * ---------- ここに登録したいチェックを並べる ----------
		 */
		
	}

	
	
}
