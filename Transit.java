import java.util.ArrayList;

/**
 * This class contains methods which perform various operations on a layered linked
 * list to simulate transit
 * 
 * @author Ishaan Ivaturi
 * @author Prince Rawal
 */
public class Transit {
	/**
	 * Makes a layered linked list representing the given arrays of train stations, bus
	 * stops, and walking locations. Each layer begins with a location of 0, even though
	 * the arrays don't contain the value 0.
	 * 
	 * @param trainStations Int array listing all the train stations
	 * @param busStops Int array listing all the bus stops
	 * @param locations Int array listing all the walking locations (always increments by 1)
	 * @return The zero node in the train layer of the final layered linked list
	 */
	public static TNode makeList(int[] trainStations, int[] busStops, int[] locations) {
		// WRITE YOUR CODE HERE	

		if (trainStations.length == 0 || busStops.length == 0 || locations.length == 0) {
			return null;
		}

		TNode t0 = new TNode (0);
		TNode t1 = new TNode (0);
		TNode t2 = new TNode (0);
		TNode ret = t2;
		t2.down = t1;
		t1.down = t0;

		int j = 0;
		int k = 0;
		for (int i = 0; i <= locations.length; i++) {

			if (i == locations.length) {
				t0.next = null;
				break;
			}
			
			t0.next = new TNode (locations[i]);
			t0.down = null;
			
			
			if (j < busStops.length && locations[i] == busStops[j]) {
				
				t1.next = new TNode (busStops[j]);
				t1.next.down = t0.next;
				if (j == busStops.length - 1) {
					t1.next.next = null;
					//break;
				}
				
				
				if (k < trainStations.length && trainStations[k] == locations[i]) {
					t2.next = new TNode (trainStations[k]);
					t2.next.down = t1.next;

					if (k == trainStations.length - 1) {
						t2.next.next = null;
					}

					t2 = t2.next;
					k++;
				}
				
				t1 = t1.next;
				j++;
			}
		
			t0 = t0.next;
		}
		
		return ret;
	}
	
	/**
	 * Modifies the given layered list to remove the given train station but NOT its associated
	 * bus stop or walking location. Do nothing if the train station doesn't exist
	 * 
	 * @param trainZero The zero node in the train layer of the given layered list
	 * @param station The location of the train station to remove
	 */
	public static void removeTrainStation(TNode trainZero, int station) {
		// WRITE YOUR CODE HERE

		if (trainZero == null) {
			return;
		}

		while (trainZero.next != null) {
			if (trainZero.next.location == station) {
				if (trainZero.next.next != null) {
					trainZero.next = trainZero.next.next;
				}
				else {
					trainZero.next = null;
					break;
				}
				
			}

			trainZero = trainZero.next;
		}
	}

	/**
	 * Modifies the given layered list to add a new bus stop at the specified location. Do nothing
	 * if there is no corresponding walking location.
	 * 
	 * @param trainZero The zero node in the train layer of the given layered list
	 * @param busStop The location of the bus stop to add
	 */
	public static void addBusStop(TNode trainZero, int busStop) {
		// WRITE YOUR CODE HERE
		if (trainZero == null) {
			return;
		}

		trainZero = trainZero.down;
		while (trainZero.next != null) {
			if (trainZero.next.location > busStop && trainZero.location < busStop) {
				TNode ins = new TNode (busStop);
				TNode walkZero = trainZero.down;
				while (walkZero.location < busStop) {
					walkZero = walkZero.next;
				}

				ins.next = trainZero.next;
				trainZero.next = ins;
				ins.down = walkZero;
			}
			trainZero = trainZero.next;
		}

		if (trainZero.location < busStop) {
			trainZero.next = new TNode (busStop);
			TNode walkZero = trainZero.down;
			while (walkZero.location < busStop) {
					walkZero = walkZero.next;
			}
			trainZero.next.down = walkZero;
			trainZero.next.next = null;
		}

	}
	
	/**
	 * Determines the optimal path to get to a given destination in the walking layer, and 
	 * collects all the nodes which are visited in this path into an arraylist. 
	 * 
	 * @param trainZero The zero node in the train layer of the given layered list
	 * @param destination An int representing the destination
	 * @return
	 */
	public static ArrayList<TNode> bestPath(TNode trainZero, int destination) {
		// WRITE YOUR CODE HERE
		ArrayList<TNode> ret = new ArrayList<TNode> ();

		if (trainZero == null) {
			return null;
		}

		while (trainZero != null) {
			ret.add(trainZero);

			if (trainZero.location == destination) {
				ret.remove(trainZero);
				while (trainZero != null) {
					ret.add(trainZero);
					trainZero = trainZero.down;
				}
				break;
			}

			if (trainZero.next == null || trainZero.next.location > destination) {
				trainZero = trainZero.down;
				ret.add(trainZero);
				if (trainZero.next == null || trainZero.next.location > destination) {
					trainZero = trainZero.down;
					ret.add(trainZero);
				}
			}

			trainZero = trainZero.next;		

		}
		
		return ret;
	}

	/**
	 * Returns a deep copy of the given layered list, which contains exactly the same
	 * locations and connections, but every node is a NEW node.
	 * 
	 * @param trainZero The zero node in the train layer of the given layered list
	 * @return
	 */
	public static TNode duplicate(TNode trainZero) {
		// WRITE YOUR CODE HERE
		if (trainZero == null) {
			return null;
		}

		
		int tsize = 0;
		int bsize = 0;
		int wsize = 0;
		TNode busZero = trainZero.down;
		TNode walkZero = trainZero.down.down;
		TNode temp = trainZero;

		while (walkZero.next != null) {
			walkZero = walkZero.next;
			wsize++;
		}
		
		while (busZero.next != null) {
			busZero = busZero.next;
			bsize++;
		}

		while (trainZero.next != null) {
			trainZero = trainZero.next;
			tsize++;
		}

		int [] trains = new int [tsize];
		int [] bus = new int [bsize];
		int [] walk = new int [wsize];
		
		TNode t0 = temp.next;
		TNode b0 = temp.down.next;
		TNode w0 = temp.down.down.next;
		
		for (int i = 0; i < tsize; i++) {
			trains[i] = t0.location;
			t0 = t0.next;
		}

		for (int i = 0; i < bsize; i++) {
			bus[i] = b0.location;
			b0 = b0.next;
		}

		for (int i = 0; i < wsize; i++) {
			walk[i] = w0.location;
			w0 = w0.next;
		}

		return makeList(trains, bus, walk);


		/*
		TNode f2 = new TNode (0);
		TNode f1 = new TNode (0);
		TNode f0 = new TNode (0);
		TNode ret = f2;
		TNode busZero = trainZero.down;
		TNode walkZero = busZero.down;

		f2.down = f1;
		f1.down = f0;

		while (walkZero.next != null) {
			if (trainZero.next != null) {
				f2.next = new TNode (trainZero.next.location);
			}

			if (busZero.next != null) {
				f1.next = new TNode (busZero.next.location);
			}
			
			f0.next = new TNode (walkZero.next.location);
			
			if (trainZero.next != null) {
				f2.next.down = f1.next;
				f2 = f2.next;
				trainZero = trainZero.next;
			}

			if (busZero.next != null) {
				f1.next.down = f0.next;
				f1 = f1.next;
				busZero = busZero.next;
			}

			f0 = f0.next;
			walkZero = walkZero.next;

		}

		return ret;
		*/

		/*
		TNode f2 = new TNode (0);
		TNode f1 = new TNode (0);
		TNode f0 = new TNode (0);
		TNode temp1 = trainZero;
		TNode temp2 = trainZero.down;

		while (trainZero.next != null) {
			f2.next = trainZero.next;
			trainZero = trainZero.next;
			f2 = f2.next;
		}

		TNode busZero = temp1.down;

		while (busZero.next != null) {
			f1.next = busZero.next;
			busZero = busZero.next;
			f1 = f1.next;
		}

		TNode walkZero = temp2.down;

		while (walkZero.next != null) {
			f0.next = walkZero.next;
			walkZero = walkZero.next;
			f0 = f0.next;
		}

		return temp1;
		*/
		

	}

	/**
	 * Modifies the given layered list to add a scooter layer in between the bus and
	 * walking layer.
	 * 
	 * @param trainZero The zero node in the train layer of the given layered list
	 * @param scooterStops An int array representing where the scooter stops are located
	 */
	public static void addScooter(TNode trainZero, int[] scooterStops) {
		// WRITE YOUR CODE HERE

		TNode fW = trainZero.down.down;
		TNode sW = new TNode (0);
		TNode bW = trainZero.down;
		int s = 0;

		if (trainZero == null || scooterStops.length == 0) {
			return;
		}
		
		//System.out.println("fW is " + fW.location + " sW is " + sW.location + " bW is " + bW.location + " s = " + s);

		while (fW != null) {
			if (fW.location == sW.location) {
				sW.down = fW;
				if (s < scooterStops.length) {
					sW.next = new TNode (scooterStops[s]);
				}
				else {
					sW.next = null;
				}
				
				
				if (bW != null && bW.location == sW.location) {
					bW.down = sW;
					if (bW.next != null) {
						bW = bW.next;
					}
						
					
				}
				
				//else {
					/*
					while (bW.next != null && bW.location < sW.location) {
						bW = bW.next;
						if (bW.next.location > sW.location) {
							//System.out.print("i cant work");
							if (bW.location == sW.location) {
								bW.down = sW;
								bW = bW.next;
							}
							break;
						}
					}
					*/
				//}
				

				
				if (s < scooterStops.length) {
					sW = sW.next;
					s++;
				}
				
			}

			fW = fW.next;
			/*
			if (fW != null) {
				System.out.print("fW is " + fW.location);
			}
			if (sW != null) {
				System.out.print(" sW is " + sW.location);
			}
			if (bW != null) {
				System.out.print(" bW is " + bW.location);
			}
			System.out.println(" s = " + s);
			*/
		}

		/*
		bW.next.down = sW.next;
		sW.down.next = fW.next;
		*/
	}
}

///JESUS CHIRST IM FINALLY DONE 