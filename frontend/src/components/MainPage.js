import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const MainPage = () => {
    const [address, setAddress] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        const script = document.createElement('script');
        script.src = "//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js";
        script.async = true;
        script.onload = () => {
            document.getElementById("address_kakao").addEventListener("click", function() {
                new window.daum.Postcode({
                    oncomplete: function(data) {
                        setAddress(data.address);
                        document.getElementById("address_kakao").value = data.address;
                    }
                }).open();
            });
        };
        document.body.appendChild(script);

        return () => {
            document.body.removeChild(script);
        };
    }, []);

    const handleSubmit = async (event) => {
        event.preventDefault();
        if (!address || address.trim() === "") {
            alert("주소를 입력해주세요.");
            return;
        }
        try {
            const response = await axios.post('/search', { address });
            const results = Array.isArray(response.data) ? response.data : [];
            navigate('/results', { state: { results } });
        } catch (error) {
            console.error('There was an error!', error);
        }
    };

    return (
        <div className="container my-3">
            <div>
                <h2>Cafe Recommendation</h2>
            </div>
            <div className="body">
                <form onSubmit={handleSubmit}>
                    <div>
                        <label htmlFor="address">
                            주소를 입력하시면 주소 기준으로 가까운 카페의 위치 최대 3곳 추천드립니다.
                        </label>
                        <input
                            type="text"
                            className="form-control"
                            id="address_kakao"
                            name="address"
                            placeholder="주소(지번 또는 도로명)를 입력하세요. ex) 서울특별시 성북구 종암로 10길"
                            readOnly
                        />
                    </div>
                    <div className="my-3">
                        <button type="submit" className="btn btn-primary" id="btn-save">
                            Search
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default MainPage;
