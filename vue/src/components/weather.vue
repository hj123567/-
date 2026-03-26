<template>
  <div class="weather-container">
    <div id="he-plugin-standard"></div>
    <div class="weather-details" v-if="weatherInfo">
      <div class="weather-item">
        <el-icon class="weather-icon"><Sunny /></el-icon>
        <span class="weather-label">今日温度:</span>
        <span class="weather-value">{{ weatherInfo.tempRange }}</span>
      </div>
      <div class="weather-item">
        <el-icon class="weather-icon"><Cloud /></el-icon>
        <span class="weather-label">天气状况:</span>
        <span class="weather-value">{{ weatherInfo.condition }}</span>
      </div>
      <div class="weather-item">
        <el-icon class="weather-icon"><Wind /></el-icon>
        <span class="weather-label">风力等级:</span>
        <span class="weather-value">{{ weatherInfo.wind }}</span>
      </div>
      <div class="weather-item">
        <el-icon class="weather-icon"><Watermelon /></el-icon>
        <span class="weather-label">湿度:</span>
        <span class="weather-value">{{ weatherInfo.humidity }}</span>
      </div>
    </div>
    <div class="weather-loading" v-else>
      <el-icon class="loading-icon"><Loading /></el-icon>
      <span>加载天气信息...</span>
    </div>
  </div>
</template>

<script>
export default {
  name: "weather",
  data() {
    return {
      weatherInfo: null
    };
  },
  created() {
    // 和风天气插件调用
    window.WIDGET = {
      "CONFIG": {
        "layout": "3", // 调整布局类型，显示更多信息
        "width": "380",
        "height": "200", // 增加高度以显示更多信息
        "background": "1", // 更改背景以获得更好的视觉效果
        "dataColor": "333333", // 更改文字颜色以提高可读性
        "borderRadius": "12", // 增加圆角以匹配整体设计
        "key": "d9bc14a738454ed08a67ab8e21daaf79"
      }
    };
    (function (d) {
      var c = d.createElement('link')
      c.rel = 'stylesheet'
      c.href = 'https://widget.heweather.net/standard/static/css/he-standard.css?v=1.4.0'
      var s = d.createElement('script')
      s.src = 'https://widget.heweather.net/standard/static/js/he-standard.js?v=1.4.0'
      var sn = d.getElementsByTagName('script')[0]
      sn.parentNode.insertBefore(c, sn)
      sn.parentNode.insertBefore(s, sn)
    })(document)
    
    // 获取真实天气数据
    this.getRealWeatherData();
  },
  methods: {
    // 获取真实天气数据
    getRealWeatherData() {
      // 先尝试获取用户位置
      this.getUserLocation();
    },
    
    // 获取用户位置
    getUserLocation() {
      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(
          (position) => {
            const latitude = position.coords.latitude;
            const longitude = position.coords.longitude;
            this.getWeatherByLocation(latitude, longitude);
          },
          (error) => {
            console.error('获取位置失败:', error);
            // 如果获取位置失败，使用默认城市
            this.getWeatherByCity('101010100'); // 北京
          }
        );
      } else {
        console.error('浏览器不支持地理位置API');
        // 如果浏览器不支持地理位置，使用默认城市
        this.getWeatherByCity('101010100'); // 北京
      }
    },
    
    // 根据经纬度获取天气
    getWeatherByLocation(latitude, longitude) {
      const key = 'd9bc14a738454ed08a67ab8e21daaf79';
      const location = `${longitude},${latitude}`;
      
      const url = `https://devapi.qweather.com/v7/weather/now?location=${location}&key=${key}`;
      
      this.fetchWeatherData(url);
    },
    
    // 根据城市ID获取天气
    getWeatherByCity(cityId) {
      const key = 'd9bc14a738454ed08a67ab8e21daaf79';
      const location = cityId;
      
      const url = `https://devapi.qweather.com/v7/weather/now?location=${location}&key=${key}`;
      
      this.fetchWeatherData(url);
    },
    
    // 通用的天气数据获取方法
    fetchWeatherData(url) {
      fetch(url)
        .then(response => response.json())
        .then(data => {
          if (data.code === '200') {
            // 获取实时天气数据
            const now = data.now;
            
            // 构建天气信息对象
            this.weatherInfo = {
              tempRange: `${now.temp}°C`,
              condition: now.text,
              wind: `${now.windScale}级 ${now.windDir}风`,
              humidity: `${now.humidity}%`
            };
          } else {
            console.error('获取天气数据失败:', data);
            // 如果API调用失败，使用模拟数据作为 fallback
            this.weatherInfo = {
              tempRange: "15°C - 25°C",
              condition: "晴转多云",
              wind: "3级西北风",
              humidity: "65%"
            };
          }
        })
        .catch(error => {
          console.error('获取天气数据出错:', error);
          // 如果网络错误，使用模拟数据作为 fallback
          this.weatherInfo = {
            tempRange: "15°C - 25°C",
            condition: "晴转多云",
            wind: "3级西北风",
            humidity: "65%"
          };
        });
    }
  }
}
</script>

<style scoped>
.weather-container {
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  background: white;
}

.weather-details {
  padding: 15px;
  border-top: 1px solid #f0f2f5;
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 10px;
  background: #f5f7fa;
}

.weather-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px;
  border-radius: 8px;
  background: white;
  transition: all 0.3s ease;
}

.weather-item:hover {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}

.weather-icon {
  font-size: 20px;
  color: #409EFF;
}

.weather-label {
  font-size: 14px;
  color: #606266;
  flex: 1;
}

.weather-value {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
}

/* 加载状态样式 */
.weather-loading {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px;
  gap: 10px;
  color: #606266;
  background: #f5f7fa;
}

.loading-icon {
  font-size: 24px;
  color: #409EFF;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .weather-details {
    grid-template-columns: 1fr;
  }
  
  .weather-loading {
    padding: 30px;
  }
}
</style>