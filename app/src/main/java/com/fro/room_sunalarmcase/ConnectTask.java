package com.fro.room_sunalarmcase;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.stream.Stream;

import com.fro.util.DataInput;
import com.fro.util.FRODigTube;
import com.fro.util.FROSun;
import com.fro.util.FROBody;
import com.fro.util.StreamUtil;

/**
 * Created by Jorble on 2016/3/4.
 */

public class ConnectTask extends AsyncTask<Void, Void, Void> {

	private Context context;
	TextView sun_tv;
	TextView info_tv;
	ProgressBar progressBar;

	private Float sun;
	private Boolean body;
	private byte[] read_buff;

	private Socket sunSocket;
	private Socket tubeSocket;
	private Socket buzzerSocket;
	private Socket curtainSocket;
	private Socket bodySocket;
	private Socket fanSocket;

	private boolean CIRCLE = false;

	public ConnectTask(Context context, TextView sun_tv, TextView info_tv, ProgressBar progressBar) {
		this.context = context;
		this.sun_tv = sun_tv;
		this.info_tv = info_tv;
		this.progressBar = progressBar;
	}

	/**
	 * 更新界面
	 */
	@Override
	protected void onProgressUpdate(Void... values) {
		if (sunSocket != null && tubeSocket != null && buzzerSocket != null && curtainSocket != null && bodySocket != null) {
			info_tv.setTextColor(context.getResources().getColor(R.color.green));
			info_tv.setText("连接正常！");
		} else {
			info_tv.setTextColor(context.getResources().getColor(R.color.red));
			info_tv.setText("连接失败！");
		}

		// 进度条消失
		progressBar.setVisibility(View.GONE);

		// 显示数据
		if (Const.sun != null) {
			sun_tv.setText(String.valueOf(Const.sun));
		}

		try {
			Thread.sleep(Const.time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 准备
	 */
	@Override
	protected void onPreExecute() {
		info_tv.setText("正在连接...");
	}

	/**
	 * 子线程任务
	 * 
	 * @param params
	 * @return
	 */
	@Override
	protected Void doInBackground(Void... params) {
		// 连接
		sunSocket = getSocket(Const.SUN_IP, Const.SUN_PORT);
		tubeSocket = getSocket(Const.TUBE_IP, Const.TUBE_PORT);
		buzzerSocket = getSocket(Const.BUZZER_IP, Const.BUZZER_PORT);
		curtainSocket = getSocket(Const.CURTAIN_IP, Const.CURTAIN_PORT);
		bodySocket = getSocket(Const.BODY_IP, Const.BODY_PORT);
		fanSocket = getSocket(Const.FAN_IP, Const.FAN_PORT);
		// 循环读取数据
		while (CIRCLE) {
			Const.count ++;
			try {
				// 如果全部连接成功
				if (sunSocket != null && tubeSocket != null && buzzerSocket != null && curtainSocket != null && fanSocket != null && bodySocket != null) {
					// 查询光照度
					StreamUtil.writeCommand(sunSocket.getOutputStream(), Const.SUN_CHK);
					StreamUtil.writeCommand(bodySocket.getOutputStream(), Const.BODY_CHK);
					Thread.sleep(Const.time / 2);
					read_buff = StreamUtil.readData(sunSocket.getInputStream());
					sun = FROSun.getData(Const.SUN_LEN, Const.SUN_NUM, read_buff);
					read_buff = StreamUtil.readData(bodySocket.getInputStream());
					body = FROBody.getData(Const.BODY_LEN, Const.BODY_NUM, read_buff);

					if (sun != null) {
						Const.sun = (int) (float) sun;
						if(DataInput.Xque.size() == 20) {
							DataInput.Xque.poll();
							DataInput.cnt++;
							DataInput.Xque.offer(String.valueOf(DataInput.cnt*Const.time));
							DataInput.Dataque.poll();
							DataInput.Dataque.offer(String.valueOf(Const.sun));
						}else {
							DataInput.cnt++;
							DataInput.Xque.offer(String.valueOf(DataInput.cnt*Const.time));
							DataInput.Dataque.offer(String.valueOf(Const.sun));
						}
					}

					// 数码管显示
					Const.TUBE_CMD = FRODigTube.intToCmdString(Const.sun);
					StreamUtil.writeCommand(tubeSocket.getOutputStream(), Const.TUBE_CMD);
					Thread.sleep(Const.time / 2);

					// 如果联动打开状态并且超过上限，蜂鸣器报警1s，打开窗帘
					// 如果有人经过，打开风扇
					if (Const.count % 10 == 1) {
						Log.i(Const.TAG, "Const.linkage=" + Const.linkage);
						Log.i(Const.TAG, "Const.sun=" + Const.sun);
						Log.i(Const.TAG, "Const.maxLim=" + Const.maxLim);
						Log.i(Const.TAG, "有人经过=" + body);
					}
					if (Const.linkage && Const.sun > Const.maxLim) {
						// 蜂鸣器
						if (!Const.isBuzzerOn) {
							StreamUtil.writeCommand(buzzerSocket.getOutputStream(), Const.BUZZER_ON);
							Thread.sleep(1000);
							StreamUtil.writeCommand(buzzerSocket.getOutputStream(), Const.BUZZER_OFF);
							Thread.sleep(200);
						}
						// 窗帘
						if (!Const.isCurtainOn) {
							StreamUtil.writeCommand(curtainSocket.getOutputStream(), Const.CURTAIN_ON);
							Thread.sleep(200);
						}

					} else if (body) {
						// 如果没光照报警时候感应到有人则报警
						// 蜂鸣器
						if (!Const.isBuzzerOn) {
							StreamUtil.writeCommand(buzzerSocket.getOutputStream(), Const.BUZZER_ON);
							Thread.sleep(200);
							StreamUtil.writeCommand(buzzerSocket.getOutputStream(), Const.BUZZER_OFF);
							Thread.sleep(50);
							StreamUtil.writeCommand(buzzerSocket.getOutputStream(), Const.BUZZER_ON);
							Thread.sleep(200);
							StreamUtil.writeCommand(buzzerSocket.getOutputStream(), Const.BUZZER_OFF);
							Thread.sleep(50);
							StreamUtil.writeCommand(buzzerSocket.getOutputStream(), Const.BUZZER_ON);
							Thread.sleep(200);
							StreamUtil.writeCommand(buzzerSocket.getOutputStream(), Const.BUZZER_OFF);
							Thread.sleep(50);
						}
					}

					if (body) {
						// 如果有人来
						// 风扇开始旋转
						if (!Const.isFanOn) {
							Const.isFanOn = true;
							StreamUtil.writeCommand(fanSocket.getOutputStream(), Const.FAN_ON);
							Thread.sleep(200);
						}
					} else {
						// 如果风扇打开，则关闭
						if (Const.isFanOn) {
							Const.isFanOn = false;
							StreamUtil.writeCommand(fanSocket.getOutputStream(), Const.FAN_OFF);
							Thread.sleep(200);
						}
						// 如果蜂鸣器打开，则关闭
						if (Const.isBuzzerOn) {
							Const.isBuzzerOn = false;
							StreamUtil.writeCommand(buzzerSocket.getOutputStream(), Const.BUZZER_OFF);
						}
					}
				}
				// 更新界面
				publishProgress();
				Thread.sleep(200);

			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// 最后关闭蜂鸣器，关闭风扇
		try {
			if (buzzerSocket != null) {
				Const.isBuzzerOn = false;
				StreamUtil.writeCommand(buzzerSocket.getOutputStream(), Const.BUZZER_OFF);
				StreamUtil.writeCommand(fanSocket.getOutputStream(), Const.FAN_OFF);
				Thread.sleep(200);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			closeSocket();
		}
		return null;

	}

	/**
	 * 建立连接并返回socket，若连接失败返回null
	 * 
	 * @param ip
	 * @param port
	 * @return
	 */
	private Socket getSocket(String ip, int port) {
		Socket mSocket = new Socket();
		InetSocketAddress mSocketAddress = new InetSocketAddress(ip, port);
		// socket连接
		try {
			// 设置连接超时时间为3秒
			mSocket.connect(mSocketAddress, 3000);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 检查是否连接成功
		if (mSocket.isConnected()) {
			Log.i(Const.TAG, ip + "连接成功！");
			return mSocket;
		} else {
			Log.i(Const.TAG, ip + "连接失败！");
			return null;
		}
	}

	public void setCIRCLE(boolean cIRCLE) {
		CIRCLE = cIRCLE;
	}

	@Override
	protected void onCancelled() {
		info_tv.setTextColor(context.getResources().getColor(R.color.gray));
		info_tv.setText("请点击连接！");
	}

	/**
	 * 关闭socket
	 */
	public void closeSocket() {
		try {
			if (sunSocket != null) {
				sunSocket.close();
			}
			if (tubeSocket != null) {
				tubeSocket.close();
			}
			if (buzzerSocket != null) {
				buzzerSocket.close();
			}
			if (fanSocket != null) {
				fanSocket.close();
			}
			if (curtainSocket != null) {
				curtainSocket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
