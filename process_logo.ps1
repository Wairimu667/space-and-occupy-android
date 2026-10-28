Add-Type -AssemblyName System.Drawing

$srcPath = "C:\Users\Administrator\Desktop\spaceandoccupy\input_images\image_0.png"
$destPath = "C:\Users\Administrator\Desktop\spaceandoccupy\app\src\main\res\drawable\logo.png"

$bmp = [System.Drawing.Bitmap]::FromFile($srcPath)
$newBmp = New-Object System.Drawing.Bitmap($bmp.Width, $bmp.Height, [System.Drawing.Imaging.PixelFormat]::Format32bppArgb)

$g = [System.Drawing.Graphics]::FromImage($newBmp)
$g.DrawImage($bmp, 0, 0, $bmp.Width, $bmp.Height)
$g.Dispose()
$bmp.Dispose()

for ($x = 0; $x -lt $newBmp.Width; $x++) {
    for ($y = 0; $y -lt $newBmp.Height; $y++) {
        $c = $newBmp.GetPixel($x, $y)
        $diffRG = [Math]::Abs([int]$c.R - [int]$c.G)
        $diffGB = [Math]::Abs([int]$c.G - [int]$c.B)
        $diffRB = [Math]::Abs([int]$c.R - [int]$c.B)

        # Background check: high lightness or near-grayscale light pixels (white, cream, grey ring)
        if ($c.R -gt 200 -and $c.G -gt 200 -and $c.B -gt 200) {
            $newBmp.SetPixel($x, $y, [System.Drawing.Color]::FromArgb(0, 0, 0, 0))
        } elseif ($diffRG -lt 25 -and $diffGB -lt 25 -and $diffRB -lt 25 -and $c.R -gt 130) {
            $newBmp.SetPixel($x, $y, [System.Drawing.Color]::FromArgb(0, 0, 0, 0))
        }
    }
}

$newBmp.Save($destPath, [System.Drawing.Imaging.ImageFormat]::Png)
$newBmp.Dispose()
Write-Host "Logo processed with clean alpha!"
