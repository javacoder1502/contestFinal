package quiz.Dao;

import java.util.ArrayList;
import java.util.List;

import quiz.utility.HibernateOperations;
import quiz.validate.ValidationImp;

public class QuizRepositoryImp implements QuizRepository{
	HibernateOperations hibernateOperations = new HibernateOperations();
	
	
	@Override
	public String getQuestion(String msisdn, String question_id,String cat) {
		int q_id=0;
		//getting question id if question id is null
		if(question_id==null){
			ValidationImp vi =  new ValidationImp(hibernateOperations);
			System.out.println("question_id is null getting question id ");
			int id  = vi.getQuestionid(msisdn, cat);
			System.out.println("questio id is befor increment  "+id);
			//getQuestionid will return the user last played question id so increment by 1 to display next question 
			id = id+1;
			question_id = Integer.toString(id);
			System.out.println("questio id is  after increment"+question_id);
		}else{
			//if question_id is not null than increment the question id to display next  question to the user
			/*question_id =question_id+1;*/
			q_id = Integer.parseInt(question_id); 
			q_id = q_id+1;
			question_id = q_id+"";
		}
		
		
		String query = "select question from Questions where question_id like '"+question_id+"' and cat like '"+cat+"'";
		List<Object> ls  = new ArrayList<Object>();
		ls = hibernateOperations.getListForSingleColumn(query);
		String question = (String)ls.get(0);
		return question;
	}

	@Override
	public List<Object[]> getOption(String question_id, String cat,String msisdn) {
		int q_id=0;
		//getting question id if question id is null
				if(question_id==null){
					ValidationImp vi =  new ValidationImp(hibernateOperations);
					System.out.println("question_id is null getting question id ");
					int id  = vi.getQuestionid(msisdn, cat);
					
					System.out.println("questio id is "+question_id);
					id = id+1;
					question_id = Integer.toString(id);
					System.out.println("questio id is  after increment"+question_id);
					
				}else{
					
					q_id = Integer.parseInt(question_id); 
					q_id = q_id+1;
					question_id = q_id+"";
					}
		
		
		String query	= "select optionA,optionB,optionC,optionD from Options "
				+ "where question_id  like '"+question_id+"' and cat like '"+cat+"'";
		List<Object[]> ls_ob = new ArrayList<Object[]>();
		ls_ob = hibernateOperations.getResultList(query);
		
		return ls_ob;
	}

	/////---------------pending---------------
	@Override
	public String getAnswer(String msisdn, String question_id,String users_answer,String cat) {
		String query = "select answer from Options  where question_id like '"+question_id+"'";
		
		 InsertUsersQuizDetails(msisdn,users_answer,question_id,cat);
		
		
		List<Object> ls  = new ArrayList<Object>();
		ls = hibernateOperations.getListForSingleColumn(query);
		String actual_answer  = (String)ls.get(0);
		
		if(actual_answer.equalsIgnoreCase(users_answer)){
			updateScoreAndQuestion_id(msisdn,cat,question_id,3);
		}else{
			updateScoreAndQuestion_id(msisdn,cat,question_id,-1);
		}
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getScore(String msisdn, String cat) {
		
		
		String query = "select score from Users_Score where msisdn like '"+msisdn+"' and cat like '"+cat+"'";
		List<Object> ls_ob = new ArrayList<Object>();
		ls_ob = hibernateOperations.getListForSingleColumn(query);
		if(ls_ob.isEmpty()){
			return 0;
		}
		/*String score = ls_ob.get(0);*/
		Object o = ls_ob.get(0);
		int score = (int)o;
		System.out.println("score i "+score);
		// TODO Auto-generated method stub
		return score;
	}

	@Override
	public void updateScoreAndQuestion_id(String msisdn, String cat,
			String question_id, int points) {
		 int users_current_score = getScore(msisdn,cat);
		 users_current_score = users_current_score + points;
		 
		 
		
		 
		 //checking if any entry exists in users_score table or not if not then insert other do update
		 String query_check = "select question_id from Users_Score where msisdn like '"+msisdn+"' and cat like '"+cat+"'";
		 List<Object> ls_ob = new ArrayList<Object>();
			ls_ob = hibernateOperations.getListForSingleColumn(query_check);
			if(ls_ob.isEmpty()){
				
				String sql_query = "insert into users_score(question_id,cat,msisdn,score) "
						+ "values('"+question_id+"','"+cat+"','"+msisdn+"','"+points+"')";
		   hibernateOperations.insert(sql_query);
				
			}
			
			//no antry exists so updateing db
			else{
	    String query = "update Users_Score set score ='"+users_current_score+"',question_id = '"+question_id+"' where msisdn like '"+msisdn+"' and cat like '"+cat+"'";
		hibernateOperations.update(query);
			}
	}

	@Override
	public void InsertUsersQuizDetails(String msisdn, String answer,
			String question_id, String cat) {
		String sql = "insert into users_answer(question_id,answer,cat,msisdn) values('"+question_id+"','"+answer+"','"+cat+"','"+msisdn+"')";
		 hibernateOperations.insert(sql);
		
	}

}
