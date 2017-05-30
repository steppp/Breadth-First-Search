package model.arrow;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import utility.Trigonometry;

public class Arrow extends Group {
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((indicator == null) ? 0 : indicator.hashCode());
		result = prime * result + ((line == null) ? 0 : line.hashCode());
		result = prime * result + ((radius == null) ? 0 : radius.hashCode());
		result = prime * result + ((sourceLabel == null) ? 0 : sourceLabel.hashCode());
		result = prime * result + ((targetLabel == null) ? 0 : targetLabel.hashCode());
		return result;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Arrow) {
			Arrow other = (Arrow) obj;
			
			return this.line.getStartX() == other.line.getStartX() &&
					this.line.getStartY() == other.line.getStartY() &&
					this.line.getEndX() == other.line.getEndX() &&
					this.line.getEndY() == other.line.getEndY();
		}
		
		return false;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Arrow from " + sourceLabel + " to " + targetLabel;
	}


	Line line;
	Circle indicator;
	
	Double radius;
	
	/**
	 * @return the radius
	 */
	public Double getRadius() {
		return radius;
	}


	/**
	 * @param radius the radius to set
	 */
	public void setRadius(Double radius) {
		this.getChildren().remove(this.indicator);
		
		this.radius = radius;
		this.indicator = createIndicator(this.line.getEndX(), this.line.getEndY(), radius);
	}


	String sourceLabel;
	String targetLabel;
	
	/**
	 * @return the targetLabel
	 */
	public String getTargetLabel() {
		return targetLabel;
	}


	/**
	 * @param targetLabel the targetLabel to set
	 */
	public void setTargetLabel(String targetLabel) {
		this.targetLabel = targetLabel;
	}


	/**
	 * @return the label
	 */
	public String getSourceLabel() {
		return sourceLabel;
	}


	/**
	 * @param label the label to set
	 */
	public void setSourceLabel(String label) {
		this.sourceLabel = label;
	}


	public Arrow(Double fromX, Double fromY, Double toX, Double toY, Double radius) {
		line = new Line();
		line.setStartX(fromX);
		line.setStartY(fromY);
		line.setEndX(toX);
		line.setEndY(toY);
		line.setStrokeWidth(3);
		line.setFill(Color.BLACK);
		
		this.radius = radius;
		
		indicator = createIndicator(toX, toY, radius);
		indicator.setFill(Color.BLACK);
		
		// calcolo il punto esatto in cui deve terminare la linea -- al centro del cerchio
		this.calculateActualEnd();
		
		this.getChildren().addAll(line, indicator);
	}
	
	
	private Circle createIndicator(Double toX, Double toY, Double radius) {
		
		indicator = new Circle();
		indicator.setRadius(radius);
		indicator.setCenterX(toX);
		indicator.setCenterY(toY);
		
		return indicator;
	}
	
	
	public void setColor(Color c) {
		if (this.indicator != null) {
			this.indicator.setFill(c);
			this.indicator.setStroke(c);
		}
		
		this.line.setFill(c);
		this.line.setStroke(c);
	}
	
	
	public Color getColor() {
		return (Color) ((Shape) this.getChildren().get(0)).getFill();
	}
	
	
	public void setStart(Double xPos, Double yPos) {
		this.line.setStartX(xPos);
		this.line.setStartY(yPos);
	}
	
	
	public void setEnd(Double xPos, Double yPos) {
		this.line.setEndX(xPos);
		this.line.setEndY(yPos);
		this.indicator.setCenterX(xPos);
		this.indicator.setCenterY(yPos);
	}
	
	
	/**
	 * Imposta la posizione del punto finale della linea ed il centro del cerchio in modo che
	 * il punto di destinazione della fraccia desiderato sia tangente al cerchio alla fine della linea
	 */
	private void calculateActualEnd() {
		this.calculateEndWithOffset(this.radius);
	}
	
	
	/**
	 * Imposta la posizione del punto finale della linea ed il centro del cerchio sulla stessa retta
	 * già disegnata ma sottraendo al modulo del segmento il valore di offset
	 * @param offset valore da sottrarre al modulo del segmento
	 */
	public void calculateEndWithOffset(Double offset) {
		Double radius = offset,
    			alphaAngle = Trigonometry.angularCoeffiecientInRadians(
    					this.line.getStartX(), this.line.getEndX(),
    					this.line.getStartY(), this.line.getEndY());
    	
    	Double offsetX = Trigonometry.getAdjacentEdgeLength(alphaAngle, radius),
    			offsetY = Trigonometry.getOppositeEdgeLength(alphaAngle, radius);

    	if (this.line.getStartX() > this.line.getEndX())
    		offsetX *= -1;
    	
    	if (this.line.getStartY() > this.line.getEndY())
    		offsetY *= -1;
    	
    	this.setEnd(this.line.getEndX() - offsetX, this.line.getEndY() - offsetY);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}