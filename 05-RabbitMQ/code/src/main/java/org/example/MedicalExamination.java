package org.example;

public enum MedicalExamination {
    Hip,
    Knee,
    Elbow;

    @Override
    public String toString() {
        return switch (this) {
            case Hip -> "Hip";
            case Knee -> "Knee";
            case Elbow -> "Elbow";
        };
    }

    public static MedicalExamination fromString(String s) {
        return switch (s) {
            case "Hip" -> Hip;
            case "Knee" -> Knee;
            case "Elbow" -> Elbow;
            default -> throw new IllegalArgumentException("Invalid MedicalExamination: " + s);
        };
    }

    public int examinationTime() {
        return switch (this) {
            case Hip -> 5000;
            case Knee -> 10000;
            case Elbow -> 15000;
        };
    }
}
