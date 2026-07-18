public class Reward {
    private int rewardId;
    private double amount;
    private String status;
    private String deadline;

    public Reward(int rewardId, double amount, String status, String deadline) {
        this.rewardId = rewardId;
        this.amount = amount;
        this.status = status;
        this.deadline = deadline;
    }

    public int getRewardId() {
        return rewardId;
    }

    public void setRewardId(int rewardId) {
        this.rewardId = rewardId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }
}
