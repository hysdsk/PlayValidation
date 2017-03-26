package forms;

import java.util.ArrayList;
import java.util.List;

import checks.IndexCheck;
import play.data.validation.Constraints.*;
import play.data.validation.ValidationError;

/**
 * フォームクラス
 * @author d_rabbit
 */
public class IndexForm {
	
	/** 名前 */
	@Required
	public String name;
	
	/** 年齢 */
	@Required
	@Min(value=15, message="15歳以上で入力してください")
	@Max(value=55, message="55歳以下で入力してください")
	public Integer age;
	
	/** メールアドレス */
	@Required
	@Email
	public String email;
	
	/** パスワード */
	@Required
	@MinLength(6)
	@MaxLength(10)
	@Pattern(value="^[0-9a-zA-Z]*$", message="英数字のみで入力してください")
	public String password;
	
	/** 再入力パスワード */
	public String againPassword;
	
	/** 職業 */
	@Required(message="選択必須です")
	public String job;
	
	/**
	 * 入力データの妥当性チェック
	 * @return エラー情報
	 */
	public List<ValidationError> validate(){
		List<ValidationError> errors = new ArrayList<>();
		IndexCheck check = new IndexCheck();
		check.execute(this, errors);
		return errors;
	}
	
}
