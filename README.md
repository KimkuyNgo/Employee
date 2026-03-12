<h2>Employee management</h2>
<p>This project is about Employee management where we are the admin and we can control each employee salary. </p>
<h2>how to deploy:</h2>
<p>first we need to deploy a database which is PostgreSQL</p>
<p>then deploy repository of the springboot project but for the first time it will crash</p>
<p>so we need to fix it. First i need to change the code in application.properties: localhost to ${PGHOST}, serverport to ${PGPORT}, name of database to ${PGDATABASE} this for url and then change username to ${PGUSER} and password to ${PGPASSWORD}</p>
<p>next we need to copy the value of PGDATABASE,PGHOST(copy url from public network domain),PGPORT(number in url from public network domain),PGPASSWORD,PGUSER from variable in PostgreSQL dtabase and paste it into variable of springboot project</p>
<p>then deploy it and it will success</p>
<p>if it fail to deploy u need to create a file name it mise.toml to having the same enviroment as railway and i use maven so i need to change my maven version to 3.9.9</p>
<p>for java u need to use version of 21.0.2</p>
<br>
<h2>what i use in this project:</h2>
<p>- Railway for deploy</p>
<p>- IDE for SpringBoot</p>
<p>- PostgreSQL for Database</p>
<p>- in Springboot i use thymeleaf, lombok, Spring web, Spring security, Spring Data JPA, PostgreSQL Driver</p>
<h2>Link to Website:</h2><br>
<a href="https://employee-production-b606.up.railway.app/">Link</a>
<h2>Data Base Figure:</h2>

@Table(name = "admins")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    @Getter
    private String username;
    @Setter
    @Getter
    private String email;
    @Setter
    @Getter
    private String password;}
<br>
    
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;
    private Double salary;
    
