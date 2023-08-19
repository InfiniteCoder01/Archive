package lfrcheats;

public class Robot {

    public String name;
    public int diameter;
    public int sensorCount;
    public int lineWeight;
    public int acuVoltage;
    public String regul;
    public int reduct;
    public int rotPerVolts;
    public int maxSpeed;
    public float distancePerSensor;
    public float distanceToLine;
    public float distanceBetweenWheels;

    public float integral = 0;
    public float prevErr = 0;

    public Robot(String name, float distanceBetweenWheels, float distanceToLine, float distancePerSensor, int diameter, int sensorCount, int lineWeight, int acuVoltage, String regul, int reduct, int rotPerVolts, int maxSpeed) {
        this.name = name;
        this.diameter = diameter;
        this.sensorCount = sensorCount;
        this.lineWeight = lineWeight;
        this.acuVoltage = acuVoltage;
        this.regul = regul;
        this.reduct = reduct;
        this.rotPerVolts = rotPerVolts;
        this.maxSpeed = maxSpeed;
        this.distancePerSensor = distancePerSensor;
        this.distanceToLine = distanceToLine;
        this.distanceBetweenWheels = distanceBetweenWheels;
    }

    public Robot() {
    }

    public float getLine(float sensorNum) {
        return sensorNum * lineWeight / sensorCount;
    }

    public int pwmToDistance(int pwm, float time) {//SM
        return (int)((pwm * acuVoltage / (float) maxSpeed * rotPerVolts / reduct * (diameter * Math.PI)) * time);
    }

    public int PID(float target, float value, float P, float I, float D) {
        float err = target - value;
        float diff = err - prevErr;
        integral += err;
        prevErr = err;
        return (int) (err * P + diff * D + integral * I);
    }

    public float getTurnAngle(int delta, float time) {
        float distanceDelta = pwmToDistance(delta, time);
        return (float) (distanceDelta / 2 / (Math.PI * distanceBetweenWheels)) * 360;
    }

    public int simulate(float P, float I, float D, int speed) {
        float angle = 0;
        float x = 0, y = 50;
        float time = (float)0.01;
        int cost = 100;
        while (true) {
            int delta = PID((sensorCount - 1) * lineWeight / 2, getLine(0), P, I, D);
            angle += getTurnAngle(delta, time);
            x += Math.cos(Math.toRadians(angle)) * speed;
            y += Math.sin(Math.toRadians(angle)) * speed;
            
            if (y == 30) {
                break;
            }
        }
        return 0;
    }

}

/*
public class Robot {

    public String name;
    public int diameter;
    public int sensorCount;
    public int lineWeight;
    public int acuVoltage;
    public String regul;
    public int reduct;
    public int rotPerVolts;
    public int maxSpeed;
    public float distancePerSensor;
    public float distanceToLine;
    public float distanceBetweenWheels;
    
    public float integral = 0;
    public float prevErr = 0;

    public Robot(String name, float distanceBetweenWheels, float distanceToLine, float distancePerSensor, int diameter, int sensorCount, int lineWeight, int acuVoltage, String regul, int reduct, int rotPerVolts, int maxSpeed) {
        this.name = name;
        this.diameter = diameter;
        this.sensorCount = sensorCount;
        this.lineWeight = lineWeight;
        this.acuVoltage = acuVoltage;
        this.regul = regul;
        this.reduct = reduct;
        this.rotPerVolts = rotPerVolts;
        this.maxSpeed = maxSpeed;
        this.distancePerSensor = distancePerSensor;
        this.distanceToLine = distanceToLine;
        this.distanceBetweenWheels = distanceBetweenWheels;
    }
    
    public Robot() {
    }
    
    public float getLine(float sensorNum){
        return sensorNum * lineWeight / sensorCount;
    }
    
    public float pwmToDistance1Sec(int pwm){//SM
        return (float)(pwm * acuVoltage / (float)maxSpeed * rotPerVolts / reduct * (diameter * Math.PI));
    }
    
    public int distanceToPwm1Sec(float distance) {
        float wheelTurns = (float) (distance / (diameter * Math.PI));
        return (int) (wheelTurns * reduct / rotPerVolts / acuVoltage * maxSpeed);
    }
    
    public float PID(float target, float value, float P, float I, float D){
        float err = target - value;
        float diff = err - prevErr;
        integral += err;
        prevErr = err;
        return err * P + diff * D + integral * I;
    }
    
    public float getP(float target, float value, float targetDelta) {
        float err = target - value;
        //err * x = targetDelta
        //x = targetDelta / err
        return targetDelta / err;
    }
    
    public float getTurnAngle(int delta, float time) {
//        int lspeed = speed - delta;
//        int rspeed = speed + delta;
//        float ldist = pwmToDistance1Sec(lspeed) * time, rdist = pwmToDistance1Sec(rspeed) * time;
//        float distanceDelta = ldist - rdist;

        float distanceDelta = pwmToDistance1Sec(delta) * time;
        return (float)(distanceDelta / 2 / (Math.PI * distanceBetweenWheels)) * 360;
    }
    
    public int getTurnDelta(float angle, float time) {
        float distanceDelta = (float) ((Math.PI * distanceBetweenWheels) * (angle / 360.0)) * 2;
        return distanceToPwm1Sec(distanceDelta / time);
    }
    
    public int getTargetDelta(float target, float value, float time){
        float err = target - value;
        //_maxValue = (numSensors - 1) * 1000;
        //(distancePerSensor * sensorCount) / (distanceToLine * 2 * PI) * 360
        float maxValue = (sensorCount - 1) * lineWeight;
        float lineAngle = (float)((distancePerSensor * sensorCount) / (distanceToLine * 2 * Math.PI) * 360);
        float errAngle = (lineAngle / 2) * (err / maxValue / 2);
        return getTurnDelta(errAngle, time);
    }
    
}
*/
