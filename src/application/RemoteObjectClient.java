package application;

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
	
	/*Nao tem construtor*/
	
	//Variavei global - o define a quantidade deexecuções do programa
	public static final int finalExecucao = 100;
	
	/*Variaveis do Calculo estatistico - Poisson */
	RandomEngine engine = new DRand(100);
	int lambda = 2;
	Poisson poisson = new Poisson(lambda, engine);
	
	//Variavel que calcula o tempo inicial de processamento
	long tempInicial;
	
	//Variaveis utilizadas para a manipulacao do arquivo
	Arquivo log = new Arquivo("C:\\Users\\Mila\\Documents\\middleware\\projetoMiddleware\\middleware\\src\\application\\logClient.txt","C:\\Users\\Mila\\Documents\\middleware\\projetoMiddleware\\middleware\\src\\application\\logClientOUT.txt");
	
	
	/*funcao responsavel por obter o tempo em segundos do sistema*/
	private int tempoSegundos(){
		/*Variaveis do calculo do tempo*/
		Calendar calendar = new GregorianCalendar();
		Date trialTime = new Date();
		calendar.setTime(trialTime);
		//int segundos = calendar.get(Calendar.SECOND);
		return calendar.get(Calendar.SECOND);
	}

	//Realizando calculo do tempo de chamada
	private int calculaTempo(){
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
		
		//Caso a soma extrapole 60 segundos, faz o uso da funçao mod
		if(resultado > 59){
			resultado = resultado % 59;
		}
		
		return resultado;
	}
	
	//Funcao que realiza o armazenamento dos dados no arquivo de log
	private void escreveString (int ID, int Poisson, long tempoProc, String resultado){
		log.println("Execucao nº: " + ID + ", Valor Poisson: " + Poisson + ", Tempo processamento: " + tempoProc + "ms, Resultado funcao: " + resultado + ".");
	}
	
	//Funcao responsavel por fechar o arquivo
	private void terminoEscrita(){
		log.close();
	}
	
	//Funcao responsavel por inicializar o contador do tempo de processamento da função
	private void inicioProcessamento(){
		tempInicial = System.currentTimeMillis(); 
	}
	
	/*Funcao responsavel por terminar a contagem do tempo de processamento e fazer a diferenca
	do tempo em que começou a funçao e que terminou. O resultado desta diferença eh apresentado 
	ao usuário.*/
	private long finalProcessamento(){
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

		//Criando um objeto cliente para poder acessar as funções do cliente
		RemoteObjectClient remoteObjectClient = new RemoteObjectClient();

		//Variaveis utilizadas para armazenar os valores
		int tempoEspera;
		int tempoAtual;
		int contadorExecucao = 0;
		long tempoProcessamento;
		String resultado;
		
		//Avisando ao usuario que o processamento comecou
		System.out.println("Iniciando processamento!");
		
		//Criando thread do cliente
		while(contadorExecucao != finalExecucao){
			
			//iniciando calculo do tempo de processamento
			remoteObjectClient.inicioProcessamento();
			
			//Chama a funcao doSomething
			resultado = remoteObjectProxy.doSomeThing();
			
			//Terminando o calculo do processamento
			tempoProcessamento = remoteObjectClient.finalProcessamento();
			
			//Calcula o tempo de chamar novamente a funcao doSomething
			tempoEspera = remoteObjectClient.calculaTempo();

			//Verifica o tempo atual do sistema em segundos
			tempoAtual = remoteObjectClient.tempoSegundos();
			 
			
			//Espera o tempo atual ser igual ao tempo de espera
			while(tempoAtual < tempoEspera){
				//Esperando os segundos antes de chamar novamente a funcao
				tempoAtual = remoteObjectClient.tempoSegundos();	
			}
					
			//Escrevendo dados no arquivo de log
			remoteObjectClient.escreveString(contadorExecucao, tempoEspera, tempoProcessamento, resultado);
			
			//contando o numero da chamada
			contadorExecucao++;
			
			//Numero da execucao
			System.out.println("Execucao: " + contadorExecucao);
			
		} //fim while
		
		//Fechando o arquivo
		remoteObjectClient.terminoEscrita();
		
		//Avisando ao usuario o final do projeto
		System.out.println("THE END!");
	} //fim Main
}
