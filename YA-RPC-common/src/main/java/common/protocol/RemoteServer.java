package common.protocol;

import java.util.List;

public class RemoteServer {

	private String serverAddress;

	private List<RemoteProcedure> procedures;

	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public List<RemoteProcedure> getProcedures() {
		return procedures;
	}

	public void setProcedures(List<RemoteProcedure> procedures) {
		this.procedures = procedures;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((procedures == null) ? 0 : procedures.hashCode());
		result = prime * result + ((serverAddress == null) ? 0 : serverAddress.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		RemoteServer other = (RemoteServer) obj;
		if (procedures == null) {
			if (other.procedures != null) {
				return false;
			}
		} else if (!procedures.equals(other.procedures)) {
			return false;
		}
		if (serverAddress == null) {
			if (other.serverAddress != null) {
				return false;
			}
		} else if (!serverAddress.equals(other.serverAddress)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "RemoteServer [serverAddress=" + serverAddress + ", procedures=" + procedures + "]";
	}

}
