package application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import cern.jet.random.Poisson;
import cern.jet.random.engine.DRand;
import cern.jet.random.engine.RandomEngine;

import commonservices.naming.NamingProxy;
import distribution.RemoteObjectProxy;


public class RemoteObjectClient {
	
	final static String logFilePath = "logClient.txt";
	
	/*Nao tem construtor*/
	
	//Variavei global - o define a quantidade deexecu��es do programa
	public static final int finalExecucao = 100;
	
	/*Variaveis do Calculo estatistico - Poisson */
	static RandomEngine engine = new DRand(100);
	static int lambda = 2;
	static Poisson poisson = new Poisson(lambda, engine);
	
	//Variavel que calcula o tempo inicial de processamento
	static long tempInicial;
	static FileWriter log;
	

	/*funcao responsavel por obter o tempo em segundos do sistema*/
	static private int tempoSegundos(){
		/*Variaveis do calculo do tempo*/
		Calendar calendar = new GregorianCalendar();
		Date trialTime = new Date();
		calendar.setTime(trialTime);
		//int segundos = calendar.get(Calendar.SECOND);
		return calendar.get(Calendar.SECOND);
	}

	//Realizando calculo do tempo de chamada
	static private int calculaTempo(){
		/*Variaveis do calculo do tempo*/
		Calendar calendar = new GregorianCalendar();
		Date trialTime = new Date();
		
		
		//Obtendo o valor da poisson
		int poissonObs = poisson.nextInt();
		
		calendar.setTime(trialTime);
		//DEBUG
		//System.out.println("Segundo: " + calendar.get(Calendar.SECOND));
		
		//Obtendo os segundos do sistema
		int segundos = calendar.get(Calendar.SECOND);
		
		//Fazendo o calculo da espera para o proximo elemento
		int resultado = segundos + poissonObs;
		
		//Caso a soma extrapole 60 segundos, faz o uso da fun�ao mod
		if(resultado > 59){
			resultado = resultado % 59;
		}
		
		return resultado;
	}
	
	//Funcao que realiza o armazenamento dos dados no arquivo de log
	static private void escreveString (int ID, int Poisson, long tempoProc, String resultado) throws IOException{
		//log.write("Execucao n�: " + ID + ", Poisson: " + Poisson + ", Tempo processamento: " + tempoProc + "ms, Resultado funcao: " + resultado + ".\n");
	}
	
	//Funcao responsavel por fechar o arquivo
	static private void terminoEscrita() throws IOException{
		log.close();
	}
	
	//Funcao responsavel por inicializar o contador do tempo de processamento da fun��o
	static private void inicioProcessamento(){
		tempInicial = System.currentTimeMillis(); 
	}
	
	/*Funcao responsavel por terminar a contagem do tempo de processamento e fazer a diferenca
	do tempo em que come�ou a fun�ao e que terminou. O resultado desta diferen�a eh apresentado 
	ao usu�rio.*/
	static private long finalProcessamento(){
		long tempFinal = System.currentTimeMillis();
		long dif = (tempFinal - tempInicial);
		return dif;
	}
	
	
	public static void main(String[] args)throws UnknownHostException,
	IOException, Throwable {

		// create an instance of Naming Service
		NamingProxy namingService = new NamingProxy("localhost", 1313);

		// check registered services
		System.out.println(namingService.list());

		// look for Calculator in Naming service
		RemoteObjectProxy remoteObjectProxy = (RemoteObjectProxy) namingService.lookup("RemoteObject");

		//Variaveis utilizadas para armazenar os valores
		int tempoEspera;
		int tempoAtual;
		int contadorExecucao = 0;
		long tempoProcessamento;
		String resultado;
		
		//Avisando ao usuario que o processamento comecou
		System.out.println("Iniciando processamento!");

		//Variaveis utilizadas para a manipulacao do arquivo
		
		log = new FileWriter(logFilePath);
		log.write("index#poissonNumber#totalTime#result\n");
		
		//Criando thread do cliente
		while(contadorExecucao != finalExecucao){
			
			//iniciando calculo do tempo de processamento
			inicioProcessamento();
			
			//Chama a funcao doSomething
			resultado = remoteObjectProxy.doSomeThing();
			
			//Terminando o calculo do processamento
			tempoProcessamento = finalProcessamento();
			
			//Calcula o tempo de chamar novamente a funcao doSomething
			tempoEspera = calculaTempo();

			//Verifica o tempo atual do sistema em segundos
			tempoAtual = tempoSegundos();
			 
			
			//Espera o tempo atual ser igual ao tempo de espera
			while(tempoAtual < tempoEspera){
				//Esperando os segundos antes de chamar novamente a funcao
				tempoAtual = tempoSegundos();	
			}
					
			//Escrevendo dados no arquivo de log
			escreveString(contadorExecucao, tempoEspera, tempoProcessamento, resultado);
			
			//contando o numero da chamada
			contadorExecucao++;
			
			//Numero da execucao
			System.out.println("Execucao: " + contadorExecucao);
			
		} //fim while
		
		//Fechando o arquivo
		terminoEscrita();
		
		//Avisando ao usuario o final do projeto
		System.out.println("THE END!");
	} //fim Main
}
