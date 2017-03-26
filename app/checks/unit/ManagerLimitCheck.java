package checks.unit;

import common.constants.Job;

public abstract class ManagerLimitCheck extends UnitCheck {
	String job;
	int age;
	public int limit;
	public ManagerLimitCheck(String job, Integer age, int limit){
		this.job = job;
		this.age = age;
		this.limit = limit;
	}
	
	@Override
	public boolean valid() {
		return Job.MANAGER.key.equals(job) && age < limit;
	}

}
