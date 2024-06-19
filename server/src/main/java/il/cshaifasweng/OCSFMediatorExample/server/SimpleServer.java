package il.cshaifasweng.OCSFMediatorExample.server;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import  il.cshaifasweng.OCSFMediatorExample.server.ocsf.*;
import org.hibernate.*;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.util.*;
import javax.persistence.criteria.*;
import java.io.IOException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class SimpleServer extends AbstractServer {

	int i = 0;
	int is = 0;
	int flag = 0;
	int j = 0;
	private static Session session;

	private static SessionFactory getSessionFactory() throws HibernateException {
		Configuration configuration = new Configuration();
		//configuration.configure("hibernate.properties");
		configuration.addAnnotatedClass(Teacher.class);
		configuration.addAnnotatedClass(Course.class);
		configuration.addAnnotatedClass(GradedExam.class);
		configuration.addAnnotatedClass(PlannedExam.class);
		configuration.addAnnotatedClass(Principal.class);
		configuration.addAnnotatedClass(Question.class);
		configuration.addAnnotatedClass(Subject.class);
		configuration.addAnnotatedClass(Time_Extention_request.class);
		configuration.addAnnotatedClass(Student.class);
		configuration.addAnnotatedClass(Exam.class);
		configuration.addAnnotatedClass(ManualResult.class);
		configuration.addAnnotatedClass(user.class);


		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
				.applySettings(configuration.getProperties()).build();
		System.out.println("end of factory");
		return configuration.buildSessionFactory(serviceRegistry);

	}

	private void generate2() {


	}
	private void generate() {

		Student student = new Student();
		student.setId(14);
		student.setName("nina");
		session.save(student);
		session.flush();
		Student student2 = new Student();
		student2.setId(15);
		student2.setName("amir");
		session.save(student2);
		session.flush();

		Subject subject1 = new Subject(77);
		subject1.setName(" math");
		session.save(subject1);
		session.flush();
		Subject subject2 = new Subject(78);
		subject1.setName(" history");
		Course course1 = new Course(22);
		session.save(course1);
		session.flush();
		course1.setSubject(subject1);
		subject1.addCourse(course1);
		student.addCourse(course1);
		Teacher teacher = new Teacher();
		teacher.setId(24);

		session.save(teacher);
		session.flush();

		teacher.addSubject(subject1);
		teacher.addCourse(course1);
		course1.setTeacher(teacher);
		Exam exam1 = new Exam("2345");

		teacher.addExam(exam1);
		Principal principal = new Principal();
		principal.setId(91);
		principal.setName("Principal Name");
		session.save(subject1);
		session.flush();
		session.save(course1);
		session.flush();

		session.save(exam1);
		session.flush();


		session.save(student);
		session.flush();
		session.save(student2);
		session.flush();
		session.save(principal);
		session.flush();

		session.save(principal);
		session.flush();


		session.save(teacher);
		session.flush();

		session.save(student);
		session.flush();

		//session.getTransaction().commit();
		Subject subject122 = new Subject(50);
		//subject122.addTeacher();
		Question question1 = new Question(subject1, 4001, "What is the capital city of France?", Arrays.asList("Rome", "Berlin", "Paris", "Madrid"), 3);
		session.save(teacher);
		session.flush();

		session.save(student);
		session.flush();

		session.save(question1);
		session.flush();


		session.getTransaction().commit();
		session.close();
	}

	public SimpleServer(int port) {
		super(port);
		//openSession();

	}


	private List<Student> getStudentList() {

		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Student> query = builder.createQuery(Student.class);
		query.from(Student.class);
		List<Student> studentList = session.createQuery(query).getResultList();
		//ystem.out.print(studentList.get(0).getName());


		return studentList;

	}

	private Student getStudentt(String studentName) {
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Student> query = builder.createQuery(Student.class);
		Root<Student> root = query.from(Student.class);
		query.select(root).where(builder.equal(root.get("name"), studentName));
		List<Student> studentList = session.createQuery(query).getResultList();

		return studentList.get(0);

	}


	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {

		try {
			System.out.println("Trying to get a session factory...");
			SessionFactory sessionFactory = getSessionFactory();
			System.out.println("Got a session factory.");

			System.out.println("Trying to open a session...");
			session = sessionFactory.openSession();
			System.out.println("Opened a session.");

			System.out.println("Trying to begin a transaction...");
			session.beginTransaction();
			System.out.println("Begun a transaction.");
		} catch (Exception e) {
			System.out.println("Exception occurred:");
			e.printStackTrace();
		}


		if (flag == 1) {
			generate();
			flag = 1;
		}
		if(msg instanceof ManualResult)
		{
			session.save((ManualResult) msg);
			session.flush();
			session.getTransaction().commit();
			session.close();



		}
		if (msg instanceof Time_Extention_request) {

			try {
				session.save((Time_Extention_request) msg);
				session.flush();
				session.getTransaction().commit();
				session.close();
			} catch (Exception e) {
				e.printStackTrace();

			}

		}
			if (msg instanceof PlannedExam) {

			try {
				session.save((PlannedExam) msg);
				session.flush();
				session.getTransaction().commit();
				session.close();
			} catch (Exception e) {
				e.printStackTrace();

			}

		}
		if (msg instanceof GradedExam) {
			try {
				GradedExam existingExam = session.get(GradedExam.class, ((GradedExam) msg).getId());
				if (existingExam != null) {
					// Update the existing exam with the new data
					GradedExam mergedExam = (GradedExam) session.merge((GradedExam) msg);
					session.getTransaction().commit();
					session.close();
				} else {

					session.save((GradedExam) msg);
					session.getTransaction().commit();
					session.close();
				}

				}catch(Exception e){
				e.printStackTrace();
			}
		}
			if (msg instanceof Question) {
			try {
				session.save((Question) msg);
				session.getTransaction().commit();
				session.close();
			} catch (Exception e) {
				e.printStackTrace();

			}


		}


			if (msg instanceof Exam) {
			Exam exx = (Exam) msg;
			try {
                //exx.setSubject();
				session.save((Exam) msg);
				session.getTransaction().commit();
				session.close();
			} catch (Exception e) {
				e.printStackTrace();

			}
		}


		if (msg instanceof String) {

				String message = (String) msg;

				System.out.println("Message from client1: " + message);
				System.out.println("after session factory");

				if (message.startsWith("addtime")) {
					try {
					//System.out.println(getClientConnections().length);
					String[] parts = message.split(":");
					int TimeREquestindex = Integer.parseInt(parts[1]);
					int idd = Integer.parseInt(parts[2]);
					int addition = Integer.parseInt(parts[3]);

					CriteriaBuilder builderz1 = session.getCriteriaBuilder();
					CriteriaQuery<Time_Extention_request> queryz1 = builderz1.createQuery(Time_Extention_request.class);
					Root<Time_Extention_request> rootz1 = queryz1.from(Time_Extention_request.class);
					queryz1.select(rootz1).where(builderz1.equal(rootz1.get("id"), TimeREquestindex));
					List<Time_Extention_request> timeExtensions = session.createQuery(queryz1).getResultList();
					if(timeExtensions.isEmpty())return;
					Time_Extention_request the_timeExtention = timeExtensions.get(0);
					Hibernate.initialize(the_timeExtention.getPlannedExam());

					the_timeExtention.setIsdone(1);
					sendToAllClients(msg);
					GradedExam mergedExam = (GradedExam) session.merge(timeExtensions.get(0));
					session.getTransaction().commit();
					session.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}


				if (message.equals("GET_STUDENT_LIST")) {

				// Retrieve the student list and send it back to the client
				List<Student> studentList = getStudentList();


				try {

					client.sendToClient(studentList);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (message.startsWith("GET_GRADES:")) {

				// Extract the selected student's name
				String selectedStudent = message.substring("GET_GRADES:".length());

				// Retrieve the student's grades and send them back to the client
				//List<Grade> studentGrade = getStudentGrades(selectedStudent);

				try {
					client.sendToClient("studentGrade");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (message.startsWith("Courses_for_subject")) {
				System.out.println("CFS reached");

				String[] parts = message.split(":");
				CriteriaBuilder builder = session.getCriteriaBuilder();
				CriteriaQuery<Subject> query = builder.createQuery(Subject.class);
				Root<Subject> root = query.from(Subject.class);
				query.select(root).where(builder.equal(root.get("id"), Integer.parseInt(parts[1])));
				List<Subject> subjects = session.createQuery(query).getResultList();
				if (subjects.isEmpty()) return;
System.out.println("cfs"+String.valueOf(subjects.get(0).getCourses().size()));
for(Course crs:subjects.get(0).getCourses())
{
	Hibernate.initialize(crs.get);
}
				try {

					client.sendToClient(subjects.get(0).getCourses());
				} catch (IOException e) {
					e.printStackTrace();
				}


			}
			if (message.startsWith("Question:")) {
				String[] parts = message.split(":");
				try {
					CriteriaBuilder builder = session.getCriteriaBuilder();
					CriteriaQuery<Question> query = builder.createQuery(Question.class);
					Root<Question> root = query.from(Question.class);

					query.select(root).where(builder.equal(root.get("id"), Integer.parseInt(parts[1])));

					query.select(root).where(builder.equal(root.get("id"), Integer.parseInt(parts[1])));
					List<Question> ques = session.createQuery(query).getResultList();
					if (ques.isEmpty()) return;
					client.sendToClient(ques.get(0));

				} catch (Exception e) {
					e.printStackTrace();
				}




			}
			if(message.startsWith("Planned"))
			{
				String[] parts = message.split(":");
				try {
					CriteriaBuilder builder = session.getCriteriaBuilder();
					CriteriaQuery<PlannedExam> query = builder.createQuery(PlannedExam.class);
					Root<PlannedExam> root = query.from(PlannedExam.class);

					query.select(root).where(builder.equal(root.get("id"), Integer.parseInt(parts[1])));

					List<PlannedExam> plannedExams = session.createQuery(query).getResultList();
					if (plannedExams.isEmpty())
					{
						client.sendToClient("plannedwrong");
						return;
					}

					Hibernate.initialize(plannedExams.get(0).getExam());
					Hibernate.initialize(plannedExams.get(0).getCourse());
					Hibernate.initialize(plannedExams.get(0).getGradedExams());
					Hibernate.initialize(plannedExams.get(0).getTeacher());
					Hibernate.initialize(plannedExams.get(0).getStartTime());
					Hibernate.initialize(plannedExams.get(0).getSubject());
					if(plannedExams.get(0).getExam()!=null) {
						Hibernate.initialize(plannedExams.get(0).getExam().getQuestions());
						Hibernate.initialize(plannedExams.get(0).getExam().getQuestionPoints());
					}


					client.sendToClient(plannedExams.get(0));

				} catch (Exception e) {
					System.out.println("Exception occurred:");
					e.printStackTrace();
				}


			}
			if (message.startsWith("Exam:")) {
				String[] parts = message.split(":");
				try {
					CriteriaBuilder builder = session.getCriteriaBuilder();
					CriteriaQuery<Exam> query = builder.createQuery(Exam.class);
					Root<Exam> root = query.from(Exam.class);

					query.select(root).where(builder.equal(root.get("id"), Integer.parseInt(parts[1])));

					List<Exam> exams = session.createQuery(query).getResultList();
					if (exams.isEmpty()) return;
					Hibernate.initialize(exams.get(0).getQuestions());
					Hibernate.initialize(exams.get(0).getQuestionPoints());
					Hibernate.initialize(exams.get(0).getCourse());

					client.sendToClient(exams.get(0));

				} catch (Exception e) {
					System.out.println("Exception occurred:");
					e.printStackTrace();
				}



			}
			if (message.startsWith("Subject:")) {
				String[] parts = message.split(":");
				try {
					CriteriaBuilder builder = session.getCriteriaBuilder();
					CriteriaQuery<Subject> query = builder.createQuery(Subject.class);
					Root<Subject> root = query.from(Subject.class);

					query.select(root).where(builder.equal(root.get("id"), Integer.parseInt(parts[1])));

					query.select(root).where(builder.equal(root.get("id"), Integer.parseInt(parts[1])));
					List<Subject> subjects = session.createQuery(query).getResultList();
					if (subjects.isEmpty()) return;
					client.sendToClient(subjects.get(0));

				} catch (Exception e) {
					System.out.println("Exception occurred:");
					e.printStackTrace();
				}



			}

			if (message.startsWith("Subjects_for_teacher:")) {
				String[] parts = message.split(":");
				try {
					CriteriaBuilder builder = session.getCriteriaBuilder();
					CriteriaQuery<Teacher> query = builder.createQuery(Teacher.class);
					Root<Teacher> root = query.from(Teacher.class);

					query.select(root).where(builder.equal(root.get("id"), Integer.parseInt(parts[1])));

					query.select(root).where(builder.equal(root.get("id"), Integer.parseInt(parts[1])));
					List<Teacher> Teachersss = session.createQuery(query).getResultList();
					System.out.println("SFT reached"+String.valueOf(Teachersss.size()));
					if (Teachersss.isEmpty()) return;
					client.sendToClient(Teachersss.get(0).getSubjects());

				} catch (Exception e) {
					System.out.println("Exception occurred:");
					e.printStackTrace();
				}






			}
			if (message.startsWith("Questions_for_exam")) {
				String[] parts = message.split(":");
				CriteriaBuilder builder = session.getCriteriaBuilder();
				CriteriaQuery<Exam> query = builder.createQuery(Exam.class);
				Root<Exam> root = query.from(Exam.class);
				query.select(root).where(builder.equal(root.get("id"), Integer.parseInt(parts[1])));
				List<Exam> examslist = session.createQuery(query).getResultList();
				if (examslist.isEmpty()) return;
				System.out.println("QfE"+String.valueOf(examslist.get(0).getQuestions().size()));

				try {

					client.sendToClient(examslist.get(0).getQuestions());
				} catch (IOException e) {
					e.printStackTrace();
				}





			}

			if (message.startsWith("Questions_for_subject")) {
				String[] parts = message.split(":");
				CriteriaBuilder builder = session.getCriteriaBuilder();
				CriteriaQuery<Subject> query = builder.createQuery(Subject.class);
				Root<Subject> root = query.from(Subject.class);
				query.select(root).where(builder.equal(root.get("id"), Integer.parseInt(parts[1])));
				List<Subject> coursesList = session.createQuery(query).getResultList();
				if (coursesList.isEmpty()) return;

				try {

					client.sendToClient(coursesList.get(0).getQuestions());
				} catch (IOException e) {
					e.printStackTrace();
				}


			}


			if (message.startsWith("UPDATE_GRADE:")) {

				// Extract the selected student, subject, and new grade
				String[] parts = message.split(":");
				String selectedStudent = parts[1];
				String subject = parts[2];
				String newGrade = parts[3];


				// Update the student's grade in the database
				//updateStudentGrade(selectedStudent, subject, Integer.parseInt(newGrade));
			}
if(message.startsWith("setlogin"))
{
	String[] parts = message.split(":");
	String user_id = parts[1];
	CriteriaBuilder builderxx = session.getCriteriaBuilder();
	CriteriaQuery<user> queryxx = builderxx.createQuery(user.class);
	Root<user> rootxx = queryxx.from(user.class);
	queryxx.select(rootxx).where(builderxx.equal(rootxx.get("id"), user_id));
	List<user>users=session.createQuery(queryxx).getResultList();
	if(!users.isEmpty())
	{
		users.get(0).setIsconnected(1);
	}




}
			if(message.startsWith("setlogout"))
			{
				String[] parts = message.split(":");
				String user_id = parts[1];
				CriteriaBuilder builderxx = session.getCriteriaBuilder();
				CriteriaQuery<user> queryxx = builderxx.createQuery(user.class);
				Root<user> rootxx = queryxx.from(user.class);
				queryxx.select(rootxx).where(builderxx.equal(rootxx.get("id"), user_id));
				List<user>users=session.createQuery(queryxx).getResultList();
				if(!users.isEmpty())
				{
					users.get(0).setIsconnected(0);
				}




			}
			if (message.startsWith("login")) {

				int Isfound = 0;
				String[] parts = message.split(":");
				String username = parts[1];
				String password = parts[2];

				CriteriaBuilder builderxx = session.getCriteriaBuilder();
				CriteriaQuery<user> queryxx = builderxx.createQuery(user.class);
				Root<user> rootxx = queryxx.from(user.class);

				queryxx.select(rootxx).where(builderxx.and(
						builderxx.equal(rootxx.get("id"), username),
						builderxx.equal(rootxx.get("password"), password)  // Assuming 'password' is the name of the attribute in your entity class and 'password' is the variable you want to compare it with.
				));
				List<user>users=session.createQuery(queryxx).getResultList();
if(users.isEmpty()){
	try {
		client.sendToClient("wrong");
	} catch (IOException e) {
		e.printStackTrace();
	}
return;
}
else if(users.get(0).getIsconnected()==1) {
	try {
		client.sendToClient("Client Is Already Connected!");
		return;
	} catch (IOException e) {
		e.printStackTrace();
	}
}
				CriteriaBuilder builder = session.getCriteriaBuilder();
				CriteriaQuery<Student> query = builder.createQuery(Student.class);
				Root<Student> root = query.from(Student.class);

				query.select(root).where(builder.equal(root.get("id"), username));
				List<Student> students = session.createQuery(query).getResultList();
				if (!students.isEmpty()) {
					try {
					Student the_student = students.get(0);
					Hibernate.initialize(the_student.getCourses());
					Hibernate.initialize(the_student.getGradedExams());
					for (GradedExam ex : the_student.getGradedExams()) {
						{
							Hibernate.initialize(ex.generateExamText());
						}
					}
					System.out.println("its a Student");
					Isfound = 1;
					//client.setInfo("String", username);
						users.get(0).setIsconnected(1);
						session.merge(users.get(0));
						session.getTransaction().commit();

						client.sendToClient(the_student);
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
				if (Isfound == 0) {
					builder = session.getCriteriaBuilder();

					CriteriaQuery<Teacher> query2 = builder.createQuery(Teacher.class);
					Root<Teacher> root2 = query2.from(Teacher.class);
					query2.select(root2).where(builder.equal(root.get("id"), username));
					List<Teacher> teachers = session.createQuery(query2).getResultList();
					if (!teachers.isEmpty()) {
						System.out.println("its teacher");
						Isfound = 1;
						users.get(0).setIsconnected(1);
						session.merge(users.get(0));
						session.getTransaction().commit();

						Teacher the_teacher = teachers.get(0);
						Hibernate.initialize(the_teacher.getSubjects());
						Hibernate.initialize(the_teacher.GetSubjectsNames());
						Hibernate.initialize(the_teacher.getExams());

						Hibernate.initialize(the_teacher.getGradedExams());
						Hibernate.initialize(the_teacher.getPlannedExams());
						Hibernate.initialize(the_teacher.getTimeExtentionRequests());
						Hibernate.initialize(the_teacher.getTimeExtentionRequests());
						Hibernate.initialize(the_teacher.getGradedExams());

						Hibernate.initialize(the_teacher.getCourses());
						Hibernate.initialize(the_teacher.getCoursesIds());
						Hibernate.initialize(the_teacher.getQuestionsinSubjectsIDDS());
						Hibernate.initialize(the_teacher.getQuestionsInsubjects());


// Access the subjects and courses collections after initialization
							List<Subject> subjects = the_teacher.getSubjects();
							List<Course> courses = the_teacher.getCourses();
                            List<Exam>exams=the_teacher.getExams();
							List<PlannedExam>pln=the_teacher.getPlannedExams();
							for(Exam exm:exams)
							{
								exm.generateExamText();
								//exm.getQuestions();
								Hibernate.initialize(exm.getPlannedExams());

							}
						for(PlannedExam ppl:pln)
						{
							Hibernate.initialize(ppl.getGradedExams());
							Hibernate.initialize(ppl.getGradedExams());
							for(GradedExam grd:ppl.getGradedExams())
							{
								Hibernate.initialize(grd.getStudent());

							}

						}
// Now you can use the subjects and courses collections as needed
// For example, iterate over the subjects
							for (Subject subject : subjects) {
								Hibernate.initialize(subject.getExams());
								Hibernate.initialize(subject.getCourses());
								Hibernate.initialize(subject.getExams());

								Hibernate.initialize(subject.getCoursesNames());
								Hibernate.initialize(subject.getQuestions());
								Hibernate.initialize(subject.getQuestionIds());



							}

// Similarly, iterate over the courses
							for (Course course : courses) {
								// Do something with each course
							}

						//client.setInfo("String", username);
						try {
							client.sendToClient(the_teacher);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

				}
				if (Isfound == 0) {
					builder = session.getCriteriaBuilder();

					CriteriaQuery<Principal> query3 = builder.createQuery(Principal.class);
					Root<Principal> root3 = query3.from(Principal.class);
					query3.select(root3).where(builder.equal(root.get("id"), username));
					List<Principal> principals = session.createQuery(query3).getResultList();
					if (!principals.isEmpty()) {
						System.out.println("its principal");

						Principal the_principal = principals.get(0);
						Isfound = 1;
						users.get(0).setIsconnected(1);
						session.merge(users.get(0));
						session.getTransaction().commit();

						//client.setInfo("String", username);
						try {
							client.sendToClient(the_principal);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}


				if (Isfound == 0) {
					try {
						client.sendToClient("wrong");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}
			if(message.startsWith("TimeRequests"))
			{
				CriteriaBuilder builder = session.getCriteriaBuilder();
				CriteriaQuery<Time_Extention_request> query = builder.createQuery(Time_Extention_request.class);
				Root<Time_Extention_request> root = query.from(Time_Extention_request.class);

				query.select(root);

				List<Time_Extention_request> curr = session.createQuery(query).getResultList();
				for (Time_Extention_request tmp:curr)
					Hibernate.initialize(tmp.getPlannedExam());
try {
	client.sendToClient(curr);
}catch (IOException e)
{
	e.printStackTrace();
}


			}


			if (message.startsWith("Time")) {


				CriteriaBuilder builderz1 = session.getCriteriaBuilder();
				CriteriaQuery<Time_Extention_request> queryz1 = builderz1.createQuery(Time_Extention_request.class);
				Root<Time_Extention_request> rootz1 = queryz1.from(Time_Extention_request.class);

				queryz1.select(rootz1);

				List<Time_Extention_request> timeExtensions = session.createQuery(queryz1).getResultList();
				try {
					client.sendToClient(timeExtensions);
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

			if (message.startsWith("RTR")) {
				int idd = Integer.parseInt(((String)((String) msg).substring(3)));
				CriteriaBuilder builderz1 = session.getCriteriaBuilder();
				CriteriaQuery<Time_Extention_request> queryz1 = builderz1.createQuery(Time_Extention_request.class);
				Root<Time_Extention_request> rootz1 = queryz1.from(Time_Extention_request.class);
				queryz1.select(rootz1).where(builderz1.equal(rootz1.get("id"), idd));
				List<Time_Extention_request> timeExtensions = session.createQuery(queryz1).getResultList();

				timeExtensions.get(0).setIsdone(1);
				GradedExam mergedExam = (GradedExam) session.merge(timeExtensions.get(0));
				session.getTransaction().commit();
				session.close();



			}
			if (message.startsWith("AllExams")) {
				CriteriaBuilder builder = session.getCriteriaBuilder();
				CriteriaQuery<Exam> query = builder.createQuery(Exam.class);
				Root<Exam> root = query.from(Exam.class);

				query.select(root);

				List<Exam> AllExams = session.createQuery(query).getResultList();
				try {
					client.sendToClient(AllExams);
				} catch (IOException e) {
					e.printStackTrace();
				}


			}
			if (message.startsWith("Exam")) {
				CriteriaBuilder builder = session.getCriteriaBuilder();
				CriteriaQuery<Exam> query = builder.createQuery(Exam.class);
				Root<Exam> root = query.from(Exam.class);

				query.select(root).where(builder.equal(root.get("id"), message.substring(4)));

				Exam curr = session.createQuery(query).getResultList().get(0);

				Hibernate.initialize(curr.getQuestions());
				Hibernate.initialize(curr.generateExamText());

				//Hibernate.initialize(curr.setQuestions());

				try {
					client.sendToClient(curr);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (message.startsWith("AllPlanned")) {
				try{
				CriteriaBuilder builder = session.getCriteriaBuilder();
				CriteriaQuery<PlannedExam> query = builder.createQuery(PlannedExam.class);
				Root<PlannedExam> root = query.from(PlannedExam.class);

				query.select(root);

				List<PlannedExam> curr = session.createQuery(query).getResultList();
				for (PlannedExam tmp:curr)
				{					Hibernate.initialize(tmp.getExam());
					//Hibernate.initialize(tmp.getExam().generateExamText());
					Hibernate.initialize(tmp.getGradedExams());
					for(GradedExam tmp2: tmp.getGradedExams())
					{
						Hibernate.initialize(tmp2.getStudent());
						//Hibernate.initialize(tmp2.getTeacher());
					//	Hibernate.initialize(tmp2.generateExamText());



					}

				}


					client.sendToClient(curr);
				} catch (IOException e) {
					e.printStackTrace();
				}



			}

			if (message.startsWith("AllGrads")) {

				CriteriaBuilder builder = session.getCriteriaBuilder();
				CriteriaQuery<GradedExam> query = builder.createQuery(GradedExam.class);
				Root<GradedExam> root = query.from(GradedExam.class);

				query.select(root);

				List<GradedExam> curr = session.createQuery(query).getResultList();
				try {
					client.sendToClient(curr);
				} catch (IOException e) {
					e.printStackTrace();
				}



			}
			if (message.startsWith("Gradesss")) {
            String student_id=((String) message).split(":")[1];
				CriteriaBuilder builder = session.getCriteriaBuilder();
				CriteriaQuery<GradedExam> query = builder.createQuery(GradedExam.class);
				Root<GradedExam> root = query.from(GradedExam.class);

				query.select(root);
				List<GradedExam> finaal=new ArrayList<>();
				List<GradedExam> curr = session.createQuery(query).getResultList();
                for(GradedExam ex:curr)
				{
					if(ex.getStudent().getId()==Integer.parseInt(student_id))finaal.add(ex);
					Hibernate.initialize(ex.generateExamText());
					Hibernate.initialize(ex.getStudent());
					Hibernate.initialize(ex.getCourse());
					Hibernate.initialize(ex.getTeacher());
					Hibernate.initialize(ex.getTeacher());
				}
				try {
					client.sendToClient(finaal);
				}catch (IOException e)
				{
					e.printStackTrace();
				}
			}

				if (message.startsWith("AllGrades")) {
				String[] parts = message.split(":");
				String selectedTeacher = parts[1];

				CriteriaBuilder builder = session.getCriteriaBuilder();
				CriteriaQuery<GradedExam> query = builder.createQuery(GradedExam.class);
				Root<GradedExam> root = query.from(GradedExam.class);

				query.select(root);

				List<GradedExam> curr = session.createQuery(query).getResultList();
				List<GradedExam> filteredExams = new ArrayList<>();

				for (GradedExam ex : curr) {
					if (ex.getTeacher().getId() == Integer.parseInt(selectedTeacher)&&ex.getCorrected()==0) {
						Hibernate.initialize(ex.generateExamText());
						Hibernate.initialize(ex.getStudent());
						Hibernate.initialize(ex.getCourse());
						Hibernate.initialize(ex.getTeacher());
						Hibernate.initialize(ex.getTeacher());

						filteredExams.add(ex);
					}
				}
				try {
					client.sendToClient(filteredExams);
				} catch (IOException e) {
					e.printStackTrace();
				}


			}
			if (message.startsWith("AllQuestions")) {
				CriteriaBuilder builder = session.getCriteriaBuilder();
				CriteriaQuery<Question> query = builder.createQuery(Question.class);
				Root<Question> root = query.from(Question.class);

				query.select(root);

				List<Question> curr = session.createQuery(query).getResultList();
				try {
					client.sendToClient(curr);
				} catch (IOException e) {
					e.printStackTrace();
				}


			}
		}
			System.out.println("XXX");
			session.getTransaction().commit();
			if (session != null && session.isOpen()) {
				session.close();
			}

		}
	private Subject getSubjectById(int id) {
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Subject> query = builder.createQuery(Subject.class);
		Root<Subject> root = query.from(Subject.class);
		query.select(root).where(builder.equal(root.get("id"), id));
		List<Subject> subjectList = session.createQuery(query).getResultList();

		return subjectList.isEmpty() ? null : subjectList.get(0);
	}
	private Course getCourseById(int id) {
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Course> query = builder.createQuery(Course.class);
		Root<Course> root = query.from(Course.class);
		query.select(root).where(builder.equal(root.get("id"), id));
		List<Course> courseList = session.createQuery(query).getResultList();

		return courseList.isEmpty() ? null : courseList.get(0);
	}
	private Exam getExamById(int id) {
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Exam> query = builder.createQuery(Exam.class);
		Root<Exam> root = query.from(Exam.class);
		query.select(root).where(builder.equal(root.get("id"), id));
		List<Exam> examList = session.createQuery(query).getResultList();

		return examList.isEmpty() ? null : examList.get(0);
	}
	private Question getQuestionById(int id) {
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Question> query = builder.createQuery(Question.class);
		Root<Question> root = query.from(Question.class);
		query.select(root).where(builder.equal(root.get("id"), id));
		List<Question> questionList = session.createQuery(query).getResultList();

		return questionList.isEmpty() ? null : questionList.get(0);
	}
	private Student getStudentById(int id) {
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Student> query = builder.createQuery(Student.class);
		Root<Student> root = query.from(Student.class);
		query.select(root).where(builder.equal(root.get("id"), id));
		List<Student> studentList = session.createQuery(query).getResultList();

		return studentList.isEmpty() ? null : studentList.get(0);
	}
	private Teacher getTeacherById(int id) {
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Teacher> query = builder.createQuery(Teacher.class);
		Root<Teacher> root = query.from(Teacher.class);
		query.select(root).where(builder.equal(root.get("id"), id));
		List<Teacher> teacherList = session.createQuery(query).getResultList();


		return teacherList.isEmpty() ? null : teacherList.get(0);
	}
	/*@Override
	protected void finalize() throws Throwable {
		//closeSession();
		//super.finalize();
	}
	private void openSession() {
		try {
			System.out.println("Trying to get a session factory...");
			SessionFactory sessionFactory = getSessionFactory();
			System.out.println("Got a session factory.");

			System.out.println("Trying to open a session...");
			this.session = sessionFactory.openSession();
			System.out.println("Opened a session.");
		} catch (Exception e) {
			System.out.println("Exception occurred:");
			e.printStackTrace();
		}
	}*/

	public void closeSession() {
		if (session != null && session.isOpen()) {
			session.close();
		}
	}
}













