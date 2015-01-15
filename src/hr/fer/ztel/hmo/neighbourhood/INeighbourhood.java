package hr.fer.ztel.hmo.neighbourhood;

import hr.fer.ztel.hmo.problem.User;

public interface INeighbourhood {
	
	public void makeMove();
	
	public void reverse();
	
	public int getDelta();

	public void makeMove(User u1, User u2);

}
