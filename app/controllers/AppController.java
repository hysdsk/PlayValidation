package controllers;

import javax.inject.Inject;

import forms.IndexForm;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.*;

import views.html.*;

/**
 * コントローラー
 * @author d_rabbit
 */
public class AppController extends Controller {

	@Inject
	private FormFactory formFactory;
	
	/**
	 * GET処理
	 * @return 遷移先画面
	 */
    public Result get() {
    	Form<IndexForm> form = formFactory.form(IndexForm.class);
        return ok(index.render(form));
    }
    
	/**
	 * POST処理
	 * @return 遷移先画面
	 */
    public Result post(){
    	Form<IndexForm> form = formFactory.form(IndexForm.class).bindFromRequest();
    	
    	if(form.hasErrors()){
    		return badRequest(index.render(form));
    	}
    	
    	/*
    	 * 入力チェックにて
    	 * エラー未検出時の処理をここに実装
    	 */
    	
    	return ok(index.render(form));
    }

}
