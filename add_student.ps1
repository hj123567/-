$body = @{
    username = "newstu001"
    password = "123456"
    name = "新学生"
    age = 20
    gender = "男"
    phoneNum = "13900001111"
    email = "newstudent@example.com"
    college = "信息工程学院"
    className = "软件工程2024-1班"
} | ConvertTo-Json

try {
    $response = Invoke-WebRequest -Uri "http://localhost:9090/stu/add" -Method POST -ContentType "application/json" -Body $body
    Write-Host "请求成功！"
    Write-Host "响应内容: $($response.Content)"
} catch {
    Write-Host "请求失败！"
    Write-Host "错误信息: $_"
    if ($_.Exception.Response) {
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $reader.BaseStream.Position = 0
        $responseContent = $reader.ReadToEnd()
        Write-Host "错误响应: $responseContent"
    }
}
