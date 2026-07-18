public class Claim {
    private int claimId;
    private String claimantName;
    private String claimantPhone;
    private String proofDescription;
    private ClaimStatus status;
    private String claimDate;
    private int itemId;

    public Claim(int claimId, String claimantName, String claimantPhone, String proofDescription, ClaimStatus status, String claimDate, int itemId) {
        this.claimId = claimId;
        this.claimantName = claimantName;
        this.claimantPhone = claimantPhone;
        this.proofDescription = proofDescription;
        this.status = status;
        this.claimDate = claimDate;
        this.itemId = itemId;
    }

    public int getClaimId() {
        return claimId;
    }

    public void setClaimId(int claimId) {
        this.claimId = claimId;
    }

    public String getClaimantName() {
        return claimantName;
    }

    public void setClaimantName(String claimantName) {
        this.claimantName = claimantName;
    }

    public String getClaimantPhone() {
        return claimantPhone;
    }

    public void setClaimantPhone(String claimantPhone) {
        this.claimantPhone = claimantPhone;
    }

    public String getProofDescription() {
        return proofDescription;
    }

    public void setProofDescription(String proofDescription) {
        this.proofDescription = proofDescription;
    }

    public ClaimStatus getStatus() {
        return status;
    }

    public void setStatus(ClaimStatus status) {
        this.status = status;
    }

    public String getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(String claimDate) {
        this.claimDate = claimDate;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
    public String toCSV() {
        return claimId + "," + claimantName + "," + claimantPhone + "," +
                proofDescription + "," + status + "," + claimDate + "," + itemId;
    }

    public static Claim fromCSV(String line) {
        String[] p = line.split(",");
        return new Claim(Integer.parseInt(p[0]), p[1], p[2], p[3],
                ClaimStatus.valueOf(p[4]), p[5], Integer.parseInt(p[6]));
    }
    @Override
    public String toString() {
        return "[Claim] ID: " + claimId + " | Claimant: " + claimantName +
                " | Item: " + itemId + " | Status: " + status;
    }
}
