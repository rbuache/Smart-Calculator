# Discovery Protocol V1.0 Specification #
    
    Author				: Buache Raphael, Frohlich Magali
    Last revision date	: 30.03.2014

## 1. Introduction ##

Compute Engine Discovery

This specification describes the protocol used by Smart Calculators to discover reachable Compute Engines in a network. Smart Calculators (clients) are terminals which execute various functionality of Compute Engines (servers). 

Smart Calculators don't know addresses of Compute Engine when starting and a new compute engine can be added in the network dynamicaly. This protocol allows calulators to access a compute engine without knowing it in advance.

To achieve this goal, Smart Calculators are listening messages from Compute Engines. In the other hand, Compute Engines send periodically a message to announce their availability. This message is sent in broadcast mode and has two goals. First, it gives the address of the Compute Engine, second, it gives information about the Compute Engine. So, the registers of reachable Compute Engines on Smart Calculators are always up to date.

----------

You will find in this specification:

- 2. Terminology
- 3. Protocol Overview
- 3.1 System Architecture
- 3.2 System Components
- 3.3 Interaction Between Components
- 4 Protocol Details
- 4.1 Transport Protocols and Connections
- 4.2 State Management
- 4.3 Message Types, Syntax and Semantics
- 4.4 Miscellaneous Considerations
- 4.5 Security Considerations
- 5 Examples
- 6 References


## 2. Terminology ##

1. Smart Calculator : It is the user interface (terminal) to interact with a Compute Engine.
2. Compute Engine : It is a server that offers services. Here, mathematical functions.
3. Listener (Discovery Listener) : It is the part of the Smart Calculator which is used to discover a Compute Engine.
4. Speaker (Discovery Speaker) : It is the part of the Compute Engine which is used to annouce itself to Smart Calculators.

## 3. Protocol Overview ##

**3.1. System Architecture**
							

Global architecture of the whole system

	|-----------------------|						|-----------------------|
	|	Smart Calculator	|						|	  Compute Engine	|
	|						|						|						|
	|	|---------------|	|		Payload			|	|---------------|	|
	|	|	Discovery	| <----------------------------	|	Discovery	|	|
	|	|	Listener	|	|		Broadcast		|	|	Speaker		|	|
	|	|---------------|	|						|	|---------------|	|
	|						|						|						|
	|						|						|						|
	|	|---------------|	|						|	|---------------|	|
	|	|	   RFC		| <--------------------------->	|	   RFC		|	|
	|	|	  Team2		|	|						|	|	  Team2		|	|
	|	|---------------|	|						|	|---------------|	|
	|						|						|						|
	|-----------------------|						|-----------------------|

Smart Calculator and Compute Engine are both a complete entity which contains two sub-systems.

1. The discovery protocol
2. The RFC Team2 is the other team's protocol used for authentication and communication between Smart Calculator and Compute Engine.

Discovery sub-system architecture

	|---------------|				|-----------|			|---------------|
	|	Discovery	| <-----------	| ----|----	| <--------	|  Discovery	|
	|	Listener	| <---\  /----	| ----|		|			|  Speaker		|
	|---------------|	   \/		|			|			|---------------|
					   	   /\		| Intranet	|
					  	  /	 \		|			|
	|---------------|	 /	  \		|			|	 		|---------------|
	|	Discovery	| <-/	   \--	| ----|		|			|  Discovery	|
	|	Listener	| <-----------	| ----|----	| <--------	|  Speaker		|
	|---------------|				|-----------|			|---------------|
								  Broadcast process


**3.2. System Components**

1. Discovery Speaker : sends messages
2. Discovery Listener : receives messages
3. Intranet : routes messages

**3.3. Interaction Between Components**

As we can see in the discovery sub-system architecture, Discovery Speaker sends broadcast messages periodically (typically each 1 to 5 seconds) and every Discovery Listener receives them.
 
There is a communication only in one direction. Discovery Speaker to Discovery Listener. In other words, Compute Engine to Smart Calculator.

## 4. Protocol Details ##

**4.1. Transport Protocols and Connections**

This protocol uses UDP to transfert messages. A Compute Engine always sends the same content in it's messages. A packet lost should not be disturbing.

UDP port on Delivery Listener : 3838.

UDP port on Delivery Sender is not significative. 

**4.2. State Management**

Stateless protocol

**4.3. Message Types, Syntax and Semantics**

The messages are data structure which contain :

	+---------------------------+-----------+---------------------------+
	|Description                |Nb of bytes|Comment                    |
	+---------------------------+-----------+---------------------------+
	|Name of the server (label) |    20     |                           |
	|Privacy                    |    1      |public==0 , private !=0    |
	+---------------------------+-----------+---------------------------+

The IP address of the Compute Engine is in the UDP header.

**4.4. Miscellaneous Considerations**

The Discover Listener should have a register with current available Compute Engines. It is updated with new messages form the Discover Speaker.

A time limit must be set in the Smart Calculator. This limit defines the time a Compute Engine information may stay in the registry without receiving updates from the Compute Engine itself. The recommended value is 30 seconds.

**4.5. Security Considerations**

There is no security during the discover process. 
The authentication comes further with the RCP Team2 protocol.

## 5. Examples ##

- DS : Discover Sender
- DL : Discover Listener

		struct{
			char name[20];
			bool privacy;
		}

		DS1: send {"neo",true} 
    	DS2: send {"niam",false}

		DL1: receive {"neo",true} 
		DL1: receive {"niam",false}
		DL2: receive {"neo",true} 
		DL2: receive {"niam",false}
		DL3: receive {"neo",true} 
		DL3: receive {"niam",false}
	

## 6. References ##

- RFC UDP   : [http://www.ietf.org/rfc/rfc768.txt](http://www.ietf.org/rfc/rfc768.txt)
- RFC Team2 : [https://github.com/yann-malherbe/Smart-Calculator/tree/master/specification](https://github.com/yann-malherbe/Smart-Calculator/tree/master/specification)