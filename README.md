この投稿ではPlayFrameworkのvalidation機能の実現について書きたいと思います。作成したソースは[GitHub](https://github.com/hys-rabbit/PlayForm)に上げてあります。
# 環境
- PlayFramework2.5
- Java8

# validation
validation機能は、画面より入力されたデータの妥当性を検証するためのWebアプリには欠かせないものです。多くのフレームワークにはvalidation機能実現をサポートする部品がデフォルトで備わっており、PlayFrameworkにおいても高機能で便利な部品が存在します。それらを使用してvalidation機能を実装しました。ファイル構成は以下の通りです。

    PlayForm
        ├── app
        │   ├── checks
        │   │   ├── Check.java
        │   │   ├── IndexCheck.java
        │   │   └── unit
        │   │       ├── EqualsCheck.java
        │   │       ├── ManagerLimitCheck.java
        │   │       └── UnitCheck.java
        │   ├── common
        │   │   └── constants
        │   │       └── Job.java
        │   ├── controllers
        │   │   └── AppController.java
        │   ├── forms
        │   │   └── IndexForm.java
        │   └── views
        │       ├── formTable.scala.html
        │       ├── index.scala.html
        │       ├── main.scala.html
        │       └── tableRowInput.scala.html
        ├── conf
        │   ├── messages
        │   └── routes
        └── public
            └── stylesheets
                └── main.css


基本的にはPlayのデフォルト構成のままですが、checksというパッケージを作りannotationで拾いきれないデータ検証ロジックを持たせるようにしてみました。

今回はFormとViewについて触れたいと思います。

# Form
formsにはFormクラスを置いています。Formのパラメータにannotationを付与するだけで、単項目チェックができてしまうことが他のフレームワークにはありますが、Playにも同様の機能があります。play.data.validation.Constraintsというパッケージがあり、必須チェックや文字数チェック、正規表現チェックなどができるので、単項目に対するチェックはほぼこれで済んでしまいます。よくありそうな項目を持たせた[IndexForm.java](https://github.com/hys-rabbit/PlayForm/blob/master/app/forms/IndexForm.java)を作ってみました。

```java:IndexForm.java
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
```
*validate()メソッドは入力値をFormにバインドする際に呼ばれるメソッドで、ここにはannotationで拾えなかったvalidation検証を実装します。checksについてはPlayFrameworkの機能とは関係のない独自の実装なので今回の投稿では触れませんが、時間のある方は覗いてみて何かの参考にして（もしくは指摘を）ください。*

##### 各annotation効果は以下の通りです
- Required： 必須チェック
- Max： 数値上限チェック
- Min： 数値下限チェック
- MaxLength： 文字数上限チェック
- MinLength： 文字数下限チェック
- Pattern： 正規表現チェック
- Email： メールアドレスチェック

他にも便利なものがあるので一度Constraintsを覗いてみると良いかと。しかし、たったこれだけで単項目に対するvalidationが実装できてしまう、なんと便利。annotationのエラー時に出力したいメッセージは外部ファイルで定義していて、それが[conf/messages](https://github.com/hys-rabbit/PlayForm/blob/master/conf/messages)になります。引数を渡しているものは、そのannotation固有の設定をしています。

   error.required = 入力必須です
   error.invalid = 正しい形式で入力してください
   error.max = {0}歳までで入力してください
   error.email = メールアドレスを入力してください
   error.minLength = {0}文字以上を入力してください
   error.maxLength = {0}文字以下を入力してください

こうしておけば、デフォルトでメッセージを定義しておくことができます。引数を渡さない場合はこのエラーメッセージを使用することになります。ここでも定義されていない場合はPlayでデフォルト定義されているメッセージが使用されます。error.invalidは入力値をFormパラメータに型違いでバインドできず失敗した時のエラーメッセージです。

# View
PlayFrameworkではscala.htmlという、いわゆる動的htmlがview部分の役割を担っています。下記はformのinputタグをtableタグで並べるだけの[index.scala.html](https://github.com/hys-rabbit/PlayForm/blob/master/app/views/index.scala.html)です。

```scala:index.scala.html
@(playForm: Form[forms.IndexForm])

@import collection.JavaConversions._
@import helper._

@main("Validation Test") {
    <h1>フォーム画面</h1>
    @form(action = routes.AppController.post()) {
        @formTable{
            @inputText(
                playForm("name"),
                '_label -> "名前"
            )(tableRowInput, implicitly[Messages])

            @inputText(
                playForm("age"),
                '_label -> "年齢",
                'placeholder -> "15-55歳"
            )(tableRowInput, implicitly[Messages])

            @inputText(
                playForm("email"),
                '_label -> "メールアドレス"
            )(tableRowInput, implicitly[Messages])

            @inputPassword(
                playForm("password"),
                '_label -> "パスワード",
                'placeholder -> "英数字のみ"
            )(tableRowInput, implicitly[Messages])

            @inputPassword(
                playForm("againPassword"),
                '_label -> "再入力パスワード"
            )(tableRowInput, implicitly[Messages])

            @select(
                playForm("job"),
                options = common.constants.Job.getMap().toSeq,
                '_label -> "職業"
            )(tableRowInput, implicitly[Messages])

        }
        <button>送信</button>
    }
}
```

Playにはviews.html.helperパッケージがあり、htmlタグをかなり調子良く生成してくれます。helperについては[公式ページ](https://www.PlayFramework.com/documentation/ja/2.0.x/JavaFormHelpers)を参照してもらえれば詳しく理解できると思います。helperを使用することで、簡単にJava側のFormと紐づけることができます。helperで生成するテンプレートにはデフォルトがあるのですが、ここではtableタグを出力したかったので[tableRowInput](https://github.com/hys-rabbit/PlayForm/blob/master/app/views/tableRowInput.scala.html)を自作してテンプレートとして使用しています。

# 動作確認
では、実際にサーバを動かして画面を見てみたいと思います。
<br/>
<img width="550" alt="スクリーンショット 2017-03-19 12.34.16.png" src="https://qiita-image-store.s3.amazonaws.com/0/169092/e961adaa-e5ee-429c-2f6a-f614a66c482e.png">


「送信」ボタンをポチッと
<br/>
<img width="550" alt="スクリーンショット 2017-03-19 12.34.16.png" src="https://qiita-image-store.s3.amazonaws.com/0/169092/d9aed71d-9e21-41fa-71cc-ef0b124c0288.png">


ちゃんとメッセージが表示されますね。

# まとめ
必ずしもフレームワークの機能を使用する必要はありませんが、生産性やメンテナンスのことを考えると、できるだけフレームワークの機能を採用するべきだと思います。むしろフレームワークの機能を使用しないのにフレームワークを使う意味もわかりません。新規開発でPlayを採用されるエンジニアの方は、是非Playの機能を活かした設計をしていただきたいと思います。

[GitHub](https://github.com/hys-rabbit/PlayForm)
