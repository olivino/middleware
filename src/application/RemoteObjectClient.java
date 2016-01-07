package application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cern.jet.random.Poisson;
import cern.jet.random.engine.DRand;
import cern.jet.random.engine.RandomEngine;

import commonservices.naming.NamingProxy;
import distribution.RemoteObjectProxy;


public class RemoteObjectClient {
	
	final static String logFilePath = "logClient.txt";
	
	/*Nao tem construtor*/
	
	//Variavei global - o define a quantidade deexecu��es do programa
	public static final int finalExecucao = 1000;
	
	/*Variaveis do Calculo estatistico - Poisson */
	static RandomEngine engine = new DRand(100);
	static int lambda = 10;
	static Poisson poisson = new Poisson(lambda, engine);
	
	//Variavel que calcula o tempo inicial de processamento
	static long tempInicial;
	static FileWriter log;
	

		
	public static void main(String[] args)throws UnknownHostException,
	IOException, Throwable {

		// create an instance of Naming Service
		NamingProxy namingService = new NamingProxy("localhost", 1313);

		// check registered services
		System.out.println(namingService.list());

		// look for Calculator in Naming service
		RemoteObjectProxy remoteObjectProxy = (RemoteObjectProxy) namingService.lookup("RemoteObject");

		//Variaveis utilizadas para armazenar os valores
		long tempoEspera;
		long tempoAtual;
		long timeNano;
		int contadorExecucao = 0;
		long tempoProcessamento;
		String resultado;
		int poissonNumber;
		
		//Avisando ao usuario que o processamento comecou
		System.out.println("Iniciando processamento!");

		//Variaveis utilizadas para a manipulacao do arquivo
		
		log = new FileWriter(logFilePath);
		log.write("index#poissonNumber#totalTime#result\n");
		
		//Criando thread do cliente
		
		timeNano = System.nanoTime();
		
		ExecutorService executor = Executors.newCachedThreadPool(); 
		
		while(contadorExecucao != finalExecucao){
			
			poissonNumber = poisson.nextInt();
			System.out.println(poissonNumber);
			tempoEspera = (long) (poissonNumber * 10E6);
			tempoAtual =  System.nanoTime() - timeNano;
			
			while(tempoEspera > tempoAtual){
				Thread.sleep(poissonNumber/2);
				tempoAtual =  System.nanoTime() - timeNano;
			}
			
			timeNano = System.nanoTime();
			Runnable task = new RemoteObjectClientThread(log, contadorExecucao, poissonNumber);
			
			executor.execute(task);
			
			contadorExecucao++;
			
			
		} //fim while
		
		executor.shutdown();
		while (!executor.isTerminated()) {
			Thread.sleep(10);
		}
		
		log.close();
		
		//Avisando ao usuario o final do projeto
		System.out.println("THE END!");
	} //fim Main
}
